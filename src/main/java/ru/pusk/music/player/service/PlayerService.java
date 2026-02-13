package ru.pusk.music.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.dto.PageDto;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.establishment.repository.EstablishmentRepository;
import ru.pusk.music.player.entity.ClientType;
import ru.pusk.music.player.entity.Player;
import ru.pusk.music.player.repository.PlayerRepository;
import ru.pusk.music.player.dto.PlayerResponse;
import ru.pusk.music.player.dto.AddPlayerResponse;
import ru.pusk.music.player.dto.PlayerChangeDto;
import ru.pusk.music.player.dto.PlayerUserDto;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerService {
  private final Set<Authority> AUTHORITIES_ACCESSED_TO_ALL_PLAYERS =
      Set.of(Authority.ROLE_ADMIN, Authority.ROLE_CLIENT_MANAGER);

  private final PlayerRepository playerRepository;
  private final EstablishmentRepository establishmentRepository;

  @PreAuthorize(
      "hasAnyAuthority("
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
          + ")")
  @Transactional
  public AddPlayerResponse addPlayer(
      Long userId, Long currentUserId, PlayerChangeDto request, Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    Player player;
    if (request.getId() == null) {
      Long establishmentId = request.getEstablishmentId();
      if (establishmentId != null) {
        playerRepository.findByEstablishmentId(establishmentId).ifPresent(playerRepository::delete);
        player = Player.builder().build();
      } else {
        throw new ServiceException("Establishment ID cannot be bull while creating a new player");
      }
    } else {
      player =
          playerRepository
              .findById(request.getId())
              .orElseThrow(() -> new ServiceException(new NotFoundError()));
      if (!player.getOwnerUserId().equals(userId) && !AccessService.isAdmin(authorities)) {
        throw new AccessDeniedException(
            "Current player with ID %s does not belong to user with ID %s"
                .formatted(player.getId(), userId));
      }
    }
    if (request.getType() != null) {
      player.setType(request.getType());
    }
    if (request.getName() != null) {
      player.setName(request.getName());
    }
    if (request.getSerialBoxNumber() != null) {
      player.setSerialBoxNumber(request.getSerialBoxNumber());
    }
    if (request.getAddress() != null) {
      player.setAddress(request.getAddress());
    }
    if (request.getVersion() != null) {
      player.setVersion(request.getVersion());
    }
    if (request.getEstablishmentId() != null) {
      Establishment establishment =
          establishmentRepository
              .findById(request.getEstablishmentId())
              .orElseThrow(() -> new ServiceException(new NotFoundError()));
      if (!establishment.getUserId().equals(userId) && !AccessService.isAdmin(authorities)) {
        throw new AccessDeniedException(
            "Couldn't add player to establishment with ID %s that does not belong to user with ID %s"
                .formatted(request.getEstablishmentId(), userId));
      }
      player.setEstablishment(establishment);
      player.setOwnerUserId(establishment.getUserId());
      playerRepository
          .findByOwnerUserIdAndSerialBoxNumberAndType(
              establishment.getUserId(), request.getSerialBoxNumber(), ClientType.MOBILE)
          .ifPresent(p -> removePlayer(p.getId(), userId, currentUserId, authorities));
    }

    Long playerId = playerRepository.save(player).getId();
    return new AddPlayerResponse(playerId);
  }


  @PreAuthorize(
      "hasAnyAuthority("
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
          + ")")
  public PlayerResponse getById(Long playerId, Long currentUserId, Set<Authority> authorities) {
    var player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (!player.getOwnerUserId().equals(currentUserId)
        && authorities.stream().noneMatch(AUTHORITIES_ACCESSED_TO_ALL_PLAYERS::contains)) {
      throw new AccessDeniedException("Access Denied");
    }
    return PlayerResponse.builder()
        .id(player.getId())
        .name(player.getName())
        .address(player.getAddress())
        .serialNumber(player.getSerialBoxNumber())
        .build();
  }




  @Transactional
  @PreAuthorize(
      "hasAnyAuthority("
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
          + ")")
  public void removePlayer(
      Long playerId, Long userId, Long currentUserId, Set<Authority> authorities) {
    if (!userId.equals(currentUserId)
        && authorities.stream().noneMatch(AUTHORITIES_ACCESSED_TO_ALL_PLAYERS::contains)) {
      throw new AccessDeniedException("Access Denied");
    }
    Player player =
        playerRepository
            .findById(playerId)
            .orElseThrow(() -> new ServiceException(new NotFoundError()));
    // TODO: send socket message = finish player
    if (!player.getOwnerUserId().equals(userId)) {
      throw new AccessDeniedException("Access denied");
    }
    playerRepository.delete(player);
  }

}
