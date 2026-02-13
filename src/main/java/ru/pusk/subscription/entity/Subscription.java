package ru.pusk.subscription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.tariff.entity.Tariff;
import ru.pusk.tariff.entity.TariffSubFeature;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "Subscription")
@Table(name = "subscription")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Timestamp createdAt;
  @OneToOne(fetch = FetchType.LAZY)
  private Establishment establishment;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tariff_id", referencedColumnName = "id", nullable = false)
  private Tariff tariff;
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_info_id")
  private OrganizationInfo info;
  private Boolean active;
  private Timestamp startDate;
  private Timestamp endDate;
  private Double price;
  private Long userId;
  private Long paymentAccountId;
  private Long paymentCardId;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @Fetch(value = FetchMode.SELECT)
  @BatchSize(size = 100)
  @JoinTable(
      name = "subscription_tariff_sub_feature",
      joinColumns = {@JoinColumn(name = "subscription_id")},
      inverseJoinColumns = {@JoinColumn(name = "tariff_sub_feature_id")}
  )
  private List<TariffSubFeature> items = new ArrayList<>();

}
