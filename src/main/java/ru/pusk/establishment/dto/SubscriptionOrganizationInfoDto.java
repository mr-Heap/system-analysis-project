package ru.pusk.establishment.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionOrganizationInfoDto {
  private Long organizationId;
  private String organizationName;
}
