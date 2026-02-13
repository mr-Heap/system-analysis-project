package ru.pusk.establishment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstablishmentStatusDto {
  private Long subId;
  private String name;
  private String status; //paid, not paid, processing
  private Timestamp writeOffDate;
  private Integer writeOffAmount;
  private Timestamp startDate;
  private Timestamp endDate;
  private Boolean active;
}
