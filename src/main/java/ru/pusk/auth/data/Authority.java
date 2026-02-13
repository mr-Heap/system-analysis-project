package ru.pusk.auth.data;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Authority {
  SIGN_AGREEMENT("sign-agreement"),
  EDIT_PERSONAL_INFO("edit-personal-info"),
  ROLE_ADMIN("role-admin"),
  ROLE_CLIENT_MANAGER("role-client-manager"),
  ROLE_CONTENT_MANAGER("role-content-manager"),
  ROLE_CLIENT("role-client"),
  WAIT_ACTIVATION("role-wait-activation"),
  GUEST("role-guest");

  @JsonValue
  private final String value;

  public static Authority fromValue(String value) {
    return Arrays.stream(Authority.values())
        .filter((authority -> authority.value.equals(value)))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("No enum constant ru.pusk.auth.data.Authority"));
  }
}
