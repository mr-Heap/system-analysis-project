package ru.pusk.tariff.dto;


import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.music.playlist.dto.TariffPlaylistDto;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private List<TariffPlaylistDto> playlists;
    private Boolean active;
    private Double price;
    private List<TariffFeatureDto> features;
}
