package ru.pusk.subscription.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionOrganizationRequestDto {
  private Long organizationId;
}
