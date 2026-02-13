package ru.pusk.music.player.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.music.player.entity.ClientType;

import java.sql.Timestamp;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
  private Long id;
  private String name;
  private String address;
  private ClientType type;
  private String serialBoxNumber;
  private String version;
  private String status;
  private Timestamp lastExchangeDataTime;
  private Timestamp shutdownTime;
  private String shutdownReason;
}
