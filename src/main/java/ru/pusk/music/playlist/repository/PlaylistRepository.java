package ru.pusk.music.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.music.playlist.entity.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
