package ru.pusk.tariff.mapper;


import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.pusk.music.playlist.dto.TariffPlaylistDto;
import ru.pusk.music.playlist.entity.Playlist;
import ru.pusk.tariff.dto.TariffFeatureDto;
import ru.pusk.tariff.dto.TariffSubFeatureDto;
import ru.pusk.tariff.entity.TariffFeature;
import ru.pusk.tariff.entity.TariffSubFeature;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TariffMapper {

    TariffFeature toTariffFeature(TariffFeatureDto dto);

    TariffSubFeature toTariffSubFeature(TariffSubFeatureDto dto);

    @InheritInverseConfiguration(name = "toTariffFeature")
    TariffFeatureDto toTariffFeatureDto(TariffFeature entity);

    TariffSubFeatureDto toTariffSubFeatureDto(TariffSubFeature entity);

    List<TariffFeatureDto> toTariffFeatureDtoList(Set<TariffFeature> entities);

    TariffPlaylistDto toTariffPlaylistDto(Playlist playlist);
}
