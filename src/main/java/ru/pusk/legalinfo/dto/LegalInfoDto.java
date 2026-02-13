package ru.pusk.legalinfo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.payment.dto.PaymentAccountDto;
import ru.pusk.payment.dto.PaymentCardDto;

import java.util.List;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class LegalInfoDto {
  private Long id;
  private String companyName;
  private String fio;
  private String inn;
  private String kpp;
  private String address;
  private String ogrn;
  private String email;
  private List<PaymentCardDto> cards;
  private List<PaymentAccountDto> accounts;
}
