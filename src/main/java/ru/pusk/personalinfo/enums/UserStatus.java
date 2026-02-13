package ru.pusk.personalinfo.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
  ACTIVE("active"),
  BLOCKED("blocked");

  @JsonValue
  private final String value;
}
