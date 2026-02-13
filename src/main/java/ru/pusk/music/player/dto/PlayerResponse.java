package ru.pusk.music.player.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlayerResponse {

  Long id;
  String name;
  String address;
  Long userId;
  String client;
  String softwareVersion;
  Boolean isOnline;
  Timestamp lastTimeOnline;
  String serialNumber;

}