package ru.pusk.tariff.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TariffPlaylistsRequestDto {

  private List<Long> playlistIds;
}
