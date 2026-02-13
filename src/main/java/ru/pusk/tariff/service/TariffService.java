package ru.pusk.tariff.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.establishment.repository.EstablishmentRepository;
import ru.pusk.music.playlist.dto.TariffPlaylistDto;
import ru.pusk.tariff.dto.*;
import ru.pusk.tariff.entity.*;
import ru.pusk.tariff.mapper.TariffMapper;
import ru.pusk.tariff.repository.TariffRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TariffService {

  private final TariffRepository tariffRepository;
  private final TariffMapper tariffMapper;
  private final EstablishmentRepository establishmentRepository;

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public List<TariffDto> getTariffs() {
    return tariffRepository.findAllByActiveIsTrue().stream().map(this::toTariffDto).toList();
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public TariffDto getEstablishmentTariff(Long userId, Long currentUserId, Long establishmentId,
      Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    Establishment establishment = establishmentRepository.findById(establishmentId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    return toTariffDto(establishment.getSubscription().getTariff());
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public TariffDto getTariffById(Long tariffId) {
    Tariff tariff = tariffRepository.findById(tariffId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    return toTariffDto(tariff);
  }

  private TariffDto toTariffDto(Tariff tariff) {
      return TariffDto.builder()
        .id(tariff.getId())
        .type(tariff.getType().getValue())
        .name(tariff.getName())
        .description(tariff.getDescription())
        .price(tariff.getPrice())
        .active(tariff.getActive())
        .playlists(tariff.getPlaylists().stream().map(tariffPlaylist -> {
                  var playlist = tariffPlaylist.getPlaylist();
                  return TariffPlaylistDto.builder()
                      .id(playlist.getId())
                      .name(playlist.getName())
                      .description(playlist.getDescription())
                      .build();
                }
            )
            .sorted(Comparator.comparing(TariffPlaylistDto::getId))
            .toList()
        )
        .features(
                tariffMapper.toTariffFeatureDtoList(tariff.getFeatures())
                        .stream()
                        .sorted(Comparator.comparing(TariffFeatureDto::getId))
                        .peek(i -> i.getItems().sort(Comparator.comparing(TariffSubFeatureDto::getId)))
                        .toList()
        )
        .build();
  }

}
