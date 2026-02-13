package ru.pusk.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccountDto {
  private Long id;
  private String accountNumber;
  private String bankBranchName;
  private String bankBranchAddress;
  private String bankBik;
  private String correspondentNumber;
}