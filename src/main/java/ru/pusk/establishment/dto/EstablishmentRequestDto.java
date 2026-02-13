package ru.pusk.establishment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstablishmentRequestDto {
  private Long id;
  private String name;
  private String address;
  private Double area;
}
