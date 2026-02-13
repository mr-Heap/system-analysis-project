package ru.pusk.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.pusk.legalinfo.entity.OrganizationInfo;

@Data
@Builder
@Entity(name = "PaymentAccount")
@Table(name = "payment_account")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String accountNumber;
  private String bankBranchName;
  private String bankBranchAddress;
  private String bankBik;
  private String correspondentNumber;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_info_id", referencedColumnName = "id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private OrganizationInfo organizationInfo;
  private Long ownerUserId;

}