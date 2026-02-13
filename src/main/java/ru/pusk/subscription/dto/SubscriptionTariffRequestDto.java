package ru.pusk.subscription.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionTariffRequestDto {
  private Long tariffId;
  private List<Long> itemIds;
}
