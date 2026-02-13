package ru.pusk.music.schedule;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.music.playlist.entity.Playlist;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByEstablishmentId(Long establishmentId);

    Optional<Schedule> findFirstByEstablishmentIdAndPlaylistId(Long establishmentId, Long playlistId);

    List<Schedule> findAllByPlaylist(Playlist playlist);

    void deleteAllByEstablishmentId(Long establishmentId);
}
