package ru.pusk.establishment.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.music.player.entity.Player;
import ru.pusk.subscription.entity.Subscription;

@Getter
@Setter
@Table(name = "establishment")
@Entity(name = "Establishment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Establishment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String address;
  @OneToOne(mappedBy = "establishment", fetch = FetchType.LAZY, optional = false)
  private Subscription subscription;
  @OneToOne(mappedBy = "establishment", fetch = FetchType.LAZY, optional = false)
  private Player player;
  private Double area;
  private String layoutFileId;
  private Long layoutFileSize;
  private Long userId;
}
