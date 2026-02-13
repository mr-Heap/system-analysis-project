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
public class SubscriptionTariffDto {
  private Long tariffId;
  private Double price;
  private String tariffName;
  private List<Long> itemIds;
}
