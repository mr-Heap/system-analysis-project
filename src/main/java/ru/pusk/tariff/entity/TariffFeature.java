package ru.pusk.tariff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity(name = "TariffFeature")
@Table(name = "tariff_feature")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffFeature {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Boolean recurrent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tariff_id", referencedColumnName = "id", nullable = false)
  private Tariff tariff;

  @OneToMany(mappedBy = "tariffFeature",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  Set<TariffSubFeature> items;
}
