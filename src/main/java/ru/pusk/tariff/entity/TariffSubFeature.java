package ru.pusk.tariff.entity;


import jakarta.persistence.*;
import lombok.*;
import ru.pusk.subscription.entity.Subscription;

import java.util.List;

@Getter
@Setter
@Entity(name = "TariffSubFeature")
@Table(name = "tariff_sub_feature")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffSubFeature {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "tariff_feature_id", referencedColumnName = "id", nullable = false)
  private TariffFeature tariffFeature;
  private Boolean approvalRequired;
  private Double price;
  private String name;

  @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
  private List<Subscription> subscriptions;
}
