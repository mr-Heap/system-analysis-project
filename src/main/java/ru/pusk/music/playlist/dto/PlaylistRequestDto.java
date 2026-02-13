package ru.pusk.music.playlist.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRequestDto {
  private Long id;
  private String name;
  private String description;
  private String titleColor;
  private Short year;
  private Set<Long> tariffs;
}
