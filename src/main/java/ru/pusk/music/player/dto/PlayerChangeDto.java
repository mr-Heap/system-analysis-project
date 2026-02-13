package ru.pusk.music.player.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.music.player.entity.ClientType;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerChangeDto {
  private Long id;
  private String name;
  private String address;
  private ClientType type;
  private String serialBoxNumber;
  private Long establishmentId;
  private String version;
}
