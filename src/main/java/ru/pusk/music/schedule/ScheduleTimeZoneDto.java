package ru.pusk.music.schedule;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTimeZoneDto {
  String city;
  String utc;
}
