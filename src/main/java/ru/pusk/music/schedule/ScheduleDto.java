package ru.pusk.music.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.common.Weekday;

import java.time.LocalTime;
import java.util.List;

@Builder
@Jacksonized
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
  private Long id;
  private List<Weekday> weekdays;
  private Long playlistId;
  private String playlistName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime startTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime endTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime startWeekendTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
  private LocalTime endWeekendTime;
  private ScheduleTimeZoneDto timezone;
}