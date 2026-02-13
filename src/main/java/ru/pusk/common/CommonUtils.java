package ru.pusk.common;

import java.util.Arrays;
import java.util.Objects;

public final class CommonUtils {

  private CommonUtils() {
  }

  @SafeVarargs
  public static <T> T concat(T... objects) {
    return Arrays.stream(objects).filter(Objects::nonNull).findFirst().orElse(null);
  }


}
