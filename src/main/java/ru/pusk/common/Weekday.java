package ru.pusk.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;

@AllArgsConstructor
@Getter
public enum Weekday {
  MON("mon", DayOfWeek.MONDAY),
  TUE("tue", DayOfWeek.TUESDAY),
  WED("wed", DayOfWeek.WEDNESDAY),
  THU("thu", DayOfWeek.THURSDAY),
  FRI("fri", DayOfWeek.FRIDAY),
  SAT("sat", DayOfWeek.SATURDAY),
  SUN("sun", DayOfWeek.SUNDAY);

  @JsonValue
  private final String value;
  private final DayOfWeek dayOfWeek;
}
