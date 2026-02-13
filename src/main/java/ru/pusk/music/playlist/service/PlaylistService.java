package ru.pusk.music.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.music.playlist.dto.AddPlaylistResponse;
import ru.pusk.music.playlist.dto.PlaylistRequestDto;
import ru.pusk.music.playlist.entity.Playlist;
import ru.pusk.music.playlist.repository.PlaylistRepository;
import ru.pusk.tariff.entity.Tariff;
import ru.pusk.tariff.entity.TariffPlaylist;
import ru.pusk.tariff.enums.TariffType;
import ru.pusk.tariff.repository.TariffRepository;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final TariffRepository tariffRepository;


  @Transactional
  @PreAuthorize("""
      hasAnyAuthority(
                  T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,
                  T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,
                  T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value
                  )
      """)
  public AddPlaylistResponse addPlaylist(PlaylistRequestDto request, Long userId) {
    Playlist playlist = request.getId() != null
        ? playlistRepository.findById(request.getId())
        .orElseThrow(() -> new ServiceException(new NotFoundError()))
        : Playlist.builder().tariffs(new ArrayList<>()).build();
    playlist.setAddedByUserId(userId);
    if (request.getName() != null) {
      playlist.setName(request.getName());
    }
    if (request.getDescription() != null) {
      playlist.setDescription(request.getDescription());
    }
    if (request.getYear() != null) {
      playlist.setYear(request.getYear());
    }
    playlist = playlistRepository.save(playlist);
    if (playlist.getTariffs() == null) {
      playlist.setTariffs(new ArrayList<>());
    }
    if (request.getTariffs() != null) {
      playlist.getTariffs().clear();
      for (Long tariffId : request.getTariffs()) {
        Tariff tariff = tariffRepository.findById(tariffId)
            .orElseThrow(() -> new ServiceException(new NotFoundError()));
        if (tariff.getType() != TariffType.INDIVIDUAL) {
          playlist.getTariffs().add(
              TariffPlaylist.builder()
                  .playlist(playlist)
                  .tariff(tariff)
                  .build()
          );
        }
      }
    }
    return new AddPlaylistResponse(playlistRepository.save(playlist).getId());
  }


  @Transactional
  @PreAuthorize("""
      hasAnyAuthority(
                  T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,
                  T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,
                  T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value
                  )
      """)
  public void deletePlaylist(PlaylistRequestDto request, Long userId) {
    playlistRepository.deleteById(request.getId());
  }
}
