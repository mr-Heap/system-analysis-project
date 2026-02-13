package ru.pusk.personalinfo.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class SubscriptionStatusDto {
  private String tariffName;
  private String status;
  @Nullable
  private Timestamp disabledFromDate;
}
