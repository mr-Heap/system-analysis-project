package ru.pusk.music.playlist.dto;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffPlaylistDto {
  private Long id;
  private String name;
  private String description;
}
