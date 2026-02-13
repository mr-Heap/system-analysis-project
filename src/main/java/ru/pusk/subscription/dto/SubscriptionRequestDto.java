package ru.pusk.subscription.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.establishment.dto.SubscriptionPaymentInfoDto;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionRequestDto {
  private Long subId;
  private SubscriptionTariffRequestDto tariffInfo;
  private SubscriptionOrganizationRequestDto organizationInfo;
  private SubscriptionPaymentInfoDto paymentInfo;
}
