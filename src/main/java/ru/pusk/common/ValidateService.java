package ru.pusk.common;

import java.util.regex.Pattern;

public class ValidateService {

  private static final Pattern EMAIL_REGULAR_EXPRESSION = Pattern.compile("^(.+)@(\\S+)\\.(\\S+)$");

  public static boolean isValidEmail(String str) {
    return str != null && EMAIL_REGULAR_EXPRESSION.matcher(str).matches();
  }

  public static boolean isValidPhone(String phone) {
    return phone != null && phone.chars().allMatch(Character::isDigit);
  }
}
