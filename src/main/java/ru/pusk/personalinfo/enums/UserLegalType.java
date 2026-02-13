package ru.pusk.personalinfo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.pusk.common.ServiceException;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserLegalType {
  NONE("none"),
  IP("individual"),
  LEGAL_ENTITY("legal");

  @JsonValue
  private final String value;

  public static UserLegalType fromString(String value) {
    return Arrays.stream(UserLegalType.values())
        .filter(it -> it.value.equals(value))
        .findAny()
        .orElseThrow(() -> new ServiceException(
            "Couldn't cast string value %s to UserLegalType".formatted(value))
        );
  }
}
