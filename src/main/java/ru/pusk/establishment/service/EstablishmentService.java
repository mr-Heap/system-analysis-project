package ru.pusk.establishment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.establishment.dto.EstablishmentRequestDto;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.establishment.dto.*;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.establishment.mapper.EstablishmentMapper;
import ru.pusk.establishment.repository.EstablishmentRepository;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.music.player.entity.Player;
import ru.pusk.music.player.repository.PlayerRepository;
import ru.pusk.music.player.dto.PlayerStatusDto;
import ru.pusk.music.player.dto.PlayerDto;
import ru.pusk.music.playlist.dto.TariffPlaylistDto;
import ru.pusk.music.playlist.entity.Playlist;
import ru.pusk.music.schedule.ScheduleRepository;
import ru.pusk.personalinfo.entity.IndividualInfo;
import ru.pusk.personalinfo.enums.UserLegalType;
import ru.pusk.personalinfo.repository.IndividualInfoRepository;
import ru.pusk.subscription.dto.SubscriptionTariffDto;
import ru.pusk.subscription.entity.Subscription;
import ru.pusk.subscription.repository.SubscriptionRepository;
import ru.pusk.tariff.entity.Tariff;
import ru.pusk.tariff.entity.TariffSubFeature;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static ru.pusk.common.CommonUtils.concat;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final IndividualInfoRepository individualInfoRepository;
    private final PlayerRepository playerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ScheduleRepository scheduleRepository;
    private final EstablishmentMapper establishmentMapper;


    @Transactional
    @PreAuthorize("hasAnyAuthority("
            + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
            + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
            + ")")
    public List<EstablishmentResponseDto> getEstablishments(String query,
                                                            Long userId, long currentUserId, Set<Authority> authorities
    ) {
        AccessService.checkAccess(userId, currentUserId, authorities);
        IndividualInfo individualInfo = individualInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(new NotFoundError()));
        return establishmentRepository.findByUserAndNameAndSubscriptionType(
                userId, query, individualInfo.getType()
        ).stream().map(this::getEstablishmentDto).toList();

    }

    private EstablishmentResponseDto getEstablishmentDto(
            Establishment establishment
    ) {
        EstablishmentResponseDto dto = establishmentMapper.toEstablishmentDto(establishment);
        Subscription subscription = establishment.getSubscription();
        if (subscription != null) {
            Tariff tariff = subscription.getTariff();
            OrganizationInfo organization = subscription.getInfo();
            SubscriptionTariffDto tariffInfo = SubscriptionTariffDto.builder()
                    .tariffId(tariff.getId())
                    .tariffName(tariff.getName())
                    .price(subscription.getPrice())
                    .itemIds(subscription.getItems().stream().map(TariffSubFeature::getId).toList())
                    .build();
            String organizationName = organization.getType() == UserLegalType.IP
                    ? organization.getFio()
                    : organization.getCompanyName();
            SubscriptionOrganizationInfoDto organizationInfo = SubscriptionOrganizationInfoDto.builder()
                    .organizationId(organization.getId())
                    .organizationName(organizationName)
                    .build();
            SubscriptionPaymentInfoDto paymentInfo = SubscriptionPaymentInfoDto.builder()
                    .cardId(subscription.getPaymentCardId())
                    .accountId(subscription.getPaymentAccountId())
                    .build();
            SubscriptionDto subscriptionInfo = SubscriptionDto.builder()
                    .id(subscription.getId())
                    .tariffInfo(tariffInfo)
                    .organizationInfo(organizationInfo)
                    .paymentInfo(paymentInfo)
                    .playlists(
                            tariff.getPlaylists().stream().map(tp -> {
                                        Playlist playlist = tp.getPlaylist();
                                        return TariffPlaylistDto.builder()
                                                .id(playlist.getId())
                                                .name(playlist.getName())
                                                .description(playlist.getDescription())
                                                .build();
                                    }).sorted(Comparator.comparing(TariffPlaylistDto::getId))
                                    .toList()
                    )
                    .build();
            dto.setSubscriptionInfo(subscriptionInfo);
        }
        Player player = establishment.getPlayer();
        if (player != null) {
            PlayerStatusDto status = PlayerStatusDto.builder()
                    .status("online")
                    .lastActivityTime(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            PlayerDto playerInfo = PlayerDto.builder()
                    .id(player.getId())
                    .type(player.getType())
                    .name(player.getName())
                    .address(player.getAddress())
                    .version(player.getVersion())
                    .serialBoxNumber(player.getSerialBoxNumber())
                    .status(status.status())
                    .build();
            if (status.status().equals("online")) {
                playerInfo.setLastExchangeDataTime(status.lastActivityTime());
            } else {
                playerInfo.setShutdownTime(status.lastActivityTime());
                playerInfo.setShutdownReason(status.shutdownReason());
            }
            dto.setPlayerInfo(
                    playerInfo
            );
        }
        return dto;
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority("
            + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
            + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
            + ")")
    public EstablishmentResponseDto getEstablishment(Long userId, Long establishmentId,
                                                     Long currentUserId,
                                                     Set<Authority> authorities
    ) {
        AccessService.checkAccess(userId, currentUserId, authorities);
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new ServiceException(new NotFoundError()));
        if (userId.equals(currentUserId) && !establishment.getUserId().equals(userId)) {
            throw new AccessDeniedException(
                    "Couldn't get establishment info. Establishment doesn't belong user %s".formatted(userId)
            );
        }
        return getEstablishmentDto(establishment);
    }


    @Transactional
    @PreAuthorize("hasAnyAuthority("
            + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
            + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
            + ")")
    public EstablishmentAddResponse addEstablishment(EstablishmentRequestDto dto,
                                                     Long userId, Long currentUserId,
                                                     Set<Authority> authorities
    ) {
        AccessService.checkAccess(userId, currentUserId, authorities);
        Establishment establishment = dto.getId() == null
                ? Establishment.builder().build()
                : establishmentRepository.findById(dto.getId())
                .orElseThrow(() -> new ServiceException(new NotFoundError()));
        establishment.setArea(concat(dto.getArea(), establishment.getArea(), 0.0));
        establishment.setAddress(concat(dto.getAddress(), establishment.getAddress(), ""));
        establishment.setName(concat(dto.getName(), establishment.getName(), ""));
        establishment.setUserId(userId);
        return new EstablishmentAddResponse(
                establishmentRepository.save(establishment).getId()
        );
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority("
            + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
            + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
            + ")")
    public void deleteEstablishment(Long userId, Long currentUserId, Long establishmentId, Set<Authority> authorities) {
        AccessService.checkAccess(userId, currentUserId, authorities);
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new ServiceException(new NotFoundError()));
        subscriptionRepository.findFirstByEstablishment(establishment).ifPresent(subscriptionRepository::delete);
        playerRepository.findByEstablishmentId(establishmentId).ifPresent(playerRepository::delete);
        scheduleRepository.deleteAllByEstablishmentId(establishmentId);
        establishmentRepository.deleteById(establishmentId);
    }
}
