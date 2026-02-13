package ru.pusk.music.playlist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pusk.tariff.entity.TariffPlaylist;

import java.util.List;


@Data
@Builder
@Entity(name = "Playlist")
@Table(name = "playlist")
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Short year;
    private String avatarFileId;
    private String summaryTrackFileId;
    private String summaryTrackFileName;
    private Long addedByUserId;

    @OneToMany(
        mappedBy = "playlist",
        fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}

    )
    private List<TariffPlaylist> tariffs;
}
