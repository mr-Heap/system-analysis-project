package ru.pusk.music.player.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;


public interface PlayerUserDto {
  Long getId();
  Long getEstablishmentId();
  Long getUserId();
  String getSerialBoxNumber();
  String getVersion();
  String getLogin();
  String getStatus();
  String getShutdownReason();
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
  Timestamp getLastExchangeDate();
}
