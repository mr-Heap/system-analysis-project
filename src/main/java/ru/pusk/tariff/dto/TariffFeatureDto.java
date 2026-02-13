package ru.pusk.tariff.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffFeatureDto {
  private Long id;
  private String name;
  private Boolean recurrent;
  private List<TariffSubFeatureDto> items;
}
