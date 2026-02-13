package ru.pusk.music.playlist.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class PlaylistMetaDto {
  private Long id;
  private String name;
  private Short version;
  private String description;
  private Short year;
  private String titleColor;
  private String avatarFileId;
  private String summaryTrackFileId;
}
