package ru.pusk.legalinfo.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.payment.entity.PaymentAccount;
import ru.pusk.personalinfo.enums.UserLegalType;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String companyName;
  private String inn;
  private String kpp;
  private String ogrn;
  private String address;
  private String email;
  private String okvd;
  private String fio;
  private Long userId;
  @Enumerated(EnumType.STRING)
  private UserLegalType type;

  @OneToMany(
      mappedBy = "organizationInfo",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<PaymentAccount> accounts = new ArrayList<>();

}
