package ru.pusk.api.http.music;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.music.schedule.ScheduleService;
import ru.pusk.music.schedule.AddScheduleResponse;
import ru.pusk.music.schedule.ScheduleDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/music/schedule")
@Slf4j
@RequiredArgsConstructor
@Tag(
    name = "Расписания",
    description = "API для управления расписаниями"
)
public class ScheduleHttpApi {


  private final ScheduleService scheduleService;

  @GetMapping("/{establishmentId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить все расписания по ID объекта"
  )
  public List<ScheduleDto> getSchedules(
      @PathVariable Long establishmentId,
      Authentication authentication
  ) {
    return scheduleService.getSchedules(
        establishmentId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{establishmentId}/change")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Добавить/изменить расписание по ID объекта"
  )
  public AddScheduleResponse addSchedule(
      @RequestBody ScheduleDto command,
      @PathVariable Long establishmentId,
      Authentication authentication
  ) {
    return scheduleService.addSchedule(
        command,
        establishmentId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{establishmentId}/{scheduleId}/remove")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Удалить расписание по ID объекта и ID расписания"
  )
  public void removeSchedule(
      @PathVariable Long establishmentId,
      @PathVariable Long scheduleId,
      Authentication authentication
  ) {
    scheduleService.removeSchedule(
        scheduleId,
        establishmentId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }
}
