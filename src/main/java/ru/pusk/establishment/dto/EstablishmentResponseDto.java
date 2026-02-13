package ru.pusk.establishment.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.music.player.dto.PlayerDto;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstablishmentResponseDto {

  private Long id;
  private String name;
  private String address;
  private Double area;
  private String layoutFileId;
  private Long layoutFileSize;
  @Nullable
  private SubscriptionDto subscriptionInfo;
  @Nullable
  private PlayerDto playerInfo;

}
