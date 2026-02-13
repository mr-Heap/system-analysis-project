package ru.pusk.tariff.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.music.playlist.entity.Playlist;

@Getter
@Setter
@Entity(name = "TariffPlaylist")
@Table(name = "tariff_playlist")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffPlaylist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tariff_id")
  private Tariff tariff;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "playlist_id")
  private Playlist playlist;

  private Long userId;
}
