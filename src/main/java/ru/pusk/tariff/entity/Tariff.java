package ru.pusk.tariff.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.tariff.enums.TariffType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Tariff")
@Table(name = "tariff")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  @Enumerated(EnumType.STRING)
  private TariffType type;
  private Boolean active;
  private Double price;
  private Long createdByUserId;
  private Short order;



  @OneToMany(
      mappedBy = "tariff",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  private Set<TariffFeature> features = new HashSet<>();


  @OneToMany(
      mappedBy = "tariff",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<TariffPlaylist> playlists;
}
