package ru.pusk.music.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.ServiceException;
import ru.pusk.common.Weekday;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.establishment.repository.EstablishmentRepository;
import ru.pusk.music.playlist.entity.Playlist;
import ru.pusk.music.playlist.repository.PlaylistRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

  private final EstablishmentRepository establishmentRepository;
  private final ScheduleRepository scheduleRepository;
  private final PlaylistRepository playlistRepository;
  private final ScheduleMapper scheduleMapper;


  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public AddScheduleResponse addSchedule(ScheduleDto scheduleDto, Long establishmentId, Long userId,
      Set<Authority> authorities
  ) {
    Establishment establishment = establishmentRepository.findById(establishmentId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (!authorities.contains(Authority.ROLE_ADMIN) && !establishment.getUserId().equals(userId)) {
      throw new AccessDeniedException("Access Denied");
    }
    Schedule schedule;
    Set<Weekday> newWeekdays = scheduleDto.getWeekdays() != null
        ? new HashSet<>(scheduleDto.getWeekdays())
        : new HashSet<>();
    if (newWeekdays.isEmpty()) {
      if (scheduleDto.getId() != null) {
        removeSchedule(scheduleDto.getId(), establishmentId, userId, authorities);
      }
      return new AddScheduleResponse(null);

    }

    if (scheduleDto.getId() != null) {
      schedule = scheduleRepository.findById(scheduleDto.getId())
          .orElseThrow(() -> new ServiceException(new NotFoundError()));
    } else if (scheduleDto.getPlaylistId() != null) {
      // scheduleDto.getId() == null
      schedule = scheduleRepository.findFirstByEstablishmentIdAndPlaylistId(
          establishmentId, scheduleDto.getPlaylistId()
      ).orElse(Schedule.builder().build());
      Set<Weekday> oldWeekDays = new HashSet<>(schedule.getEnumWeekdays());
      newWeekdays.addAll(oldWeekDays);
    } else {
      throw new ServiceException("Playlist id cannot be null");
    }
    schedule.setEstablishmentId(establishmentId);
    schedule.setWeekdays(
        newWeekdays.stream()
            .filter(Objects::nonNull)
            .map(Weekday::toString)
            .collect(Collectors.joining(","))
    );

    //TODO: remove second condition after front fix
    if (scheduleDto.getPlaylistId() != null &&
        !scheduleDto.getPlaylistId().equals(scheduleDto.getId()) &&
        !scheduleDto.getPlaylistId().equals(schedule.getId())
    ) {
      Playlist playlist = playlistRepository.findById(scheduleDto.getPlaylistId())
          .orElseThrow(() -> new ServiceException(new NotFoundError()));
      schedule.setPlaylist(playlist);
    }

    if (scheduleDto.getStartTime() != null) {
      schedule.setStartTime(scheduleDto.getStartTime());
    }
    if (scheduleDto.getEndTime() != null) {
      schedule.setEndTime(scheduleDto.getEndTime());
    }
    if (scheduleDto.getStartWeekendTime() != null) {
      schedule.setStartWeekendTime(scheduleDto.getStartWeekendTime());
    }
    if (scheduleDto.getEndWeekendTime() != null) {
      schedule.setEndWeekendTime(scheduleDto.getEndWeekendTime());
    }
    ScheduleTimeZoneDto zone = scheduleDto.getTimezone();
    if (zone != null) {
      schedule.setTimezone(zone.getCity() + "," + zone.getUtc());
    }

    return new AddScheduleResponse(scheduleRepository.save(schedule).getId());
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public void removeSchedule(Long scheduleId, Long establishmentId, Long userId,
      Set<Authority> authorities) {
    Establishment establishment = establishmentRepository.findById(establishmentId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (!authorities.contains(Authority.ROLE_ADMIN) && !establishment.getUserId().equals(userId)) {
      throw new AccessDeniedException("Access Denied");
    }
    scheduleRepository.deleteById(scheduleId);
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public List<ScheduleDto> getSchedules(Long establishmentId, Long userId,
      Set<Authority> authorities) {
    Establishment establishment = establishmentRepository.findById(establishmentId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (!authorities.contains(Authority.ROLE_ADMIN) && !establishment.getUserId().equals(userId)) {
      throw new AccessDeniedException("Access Denied");
    }
    return scheduleRepository.findAllByEstablishmentId(establishmentId)
        .stream()
        .sorted(scheduleComparator())
        .map(scheduleMapper::toScheduleDto)
        .toList();
  }

  private static Comparator<Schedule> scheduleComparator() {
    return Comparator
        .comparing((Schedule s) -> {
          if (s.getStartTime() != null) {
            return s.getStartTime();
          } else {
            return s.getStartWeekendTime();
          }
        })
        .thenComparing(s -> {
          if (s.getEndTime() != null) {
            return s.getEndTime();
          } else {
            return s.getEndWeekendTime();
          }
        });
  }
}