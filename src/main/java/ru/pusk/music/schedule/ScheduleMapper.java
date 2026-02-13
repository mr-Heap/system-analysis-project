package ru.pusk.music.schedule;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ScheduleMapper {


  @Mapping(target = "weekdays", expression = "java(schedule.getEnumWeekdays())")
  @Mapping(target = "timezone", expression = "java(schedule.getTimeZoneDto())")
  @Mapping(target = "playlistId", expression = "java(schedule.getPlaylist().getId())")
  @Mapping(target = "playlistName", expression = "java(schedule.getPlaylist().getName())")
  ScheduleDto toScheduleDto(Schedule schedule);

}
