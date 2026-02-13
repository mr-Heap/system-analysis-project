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
@Entity(name = "PaymentCard")
@Table(name = "payment_card")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String externalCardId;
  private String pan;
  private String bankLogoUrl;
  @Column(name = "re_bill_id")
  private Long reBillId;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_info_id", referencedColumnName = "id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private OrganizationInfo organizationInfo;
  private Long ownerUserId;

}
