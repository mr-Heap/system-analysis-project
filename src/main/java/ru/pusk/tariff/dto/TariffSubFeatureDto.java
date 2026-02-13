package ru.pusk.tariff.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffSubFeatureDto {
  private Long id;
  private String name;
  private Boolean approvalRequired;
  private Double price;
}
