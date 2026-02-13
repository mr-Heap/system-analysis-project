package ru.pusk.establishment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.annotation.Nullable;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SubscriptionPaymentInfoDto {
  @Nullable
  private Long cardId;
  @Nullable
  private Long accountId;
}
