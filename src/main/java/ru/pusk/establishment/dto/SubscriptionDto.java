package ru.pusk.establishment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.music.playlist.dto.TariffPlaylistDto;
import ru.pusk.subscription.dto.SubscriptionTariffDto;

import java.util.List;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionDto {
  private Long id;
  private SubscriptionTariffDto tariffInfo;
  private SubscriptionOrganizationInfoDto organizationInfo;
  private SubscriptionPaymentInfoDto paymentInfo;
  private List<TariffPlaylistDto> playlists;
}
