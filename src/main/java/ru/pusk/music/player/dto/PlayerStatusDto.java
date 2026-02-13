package ru.pusk.music.player.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;

@Jacksonized
@Builder
public record PlayerStatusDto(String status, Timestamp lastActivityTime, String shutdownReason) {

}
