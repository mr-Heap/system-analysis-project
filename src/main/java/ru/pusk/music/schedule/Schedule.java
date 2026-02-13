package ru.pusk.music.schedule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pusk.common.Weekday;
import ru.pusk.music.playlist.entity.Playlist;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@Entity(name = "Schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long establishmentId;
    @OneToOne(fetch = FetchType.EAGER)
    private Playlist playlist;
    private String weekdays;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime startWeekendTime;
    private LocalTime endWeekendTime;
    private String timezone;

    public List<Weekday> getEnumWeekdays() {
        if (weekdays == null || weekdays.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(weekdays.split(",")).map(Weekday::valueOf).toList();
    }

    public ScheduleTimeZoneDto getTimeZoneDto() {
        String[] timezone = this.timezone.split(",");
        if (timezone.length != 2) {
            throw new RuntimeException("Invalid timezone DB format: %s".formatted(this.timezone));
        }
        return ScheduleTimeZoneDto.builder()
            .city(timezone[0])
            .utc(timezone[1])
            .build();
    }
}
