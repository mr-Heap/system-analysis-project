package ru.pusk.api.http.music;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pusk.music.playlist.dto.AddPlaylistResponse;
import ru.pusk.music.playlist.dto.PlaylistMetaDto;
import ru.pusk.music.playlist.dto.PlaylistRequestDto;
import ru.pusk.music.playlist.service.PlaylistService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/music/playlist")
@Slf4j
@Tag(
        name = "Плейлисты",
        description = "API для управления плейлистами"
)
public class PlaylistHttpApi {

    private final PlaylistService playlistService;

    public PlaylistHttpApi(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }


    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Добавить плейлист (или изменить, передав ID)"
    )
    public AddPlaylistResponse addPlaylist(@RequestBody PlaylistRequestDto request,
                                           Authentication authentication
    ) {
        return playlistService.addPlaylist(request, Long.parseLong(authentication.getName()));
    }

    @PostMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Добавить плейлист (или изменить, передав ID)"
    )
    public void deletePlaylist(@RequestBody PlaylistRequestDto request,
                               Authentication authentication
    ) {
        playlistService.deletePlaylist(request, Long.parseLong(authentication.getName()));
    }
}
