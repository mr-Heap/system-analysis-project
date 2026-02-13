package ru.pusk.api.http.music;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.common.dto.PageDto;
import ru.pusk.music.player.dto.PlayerResponse;
import ru.pusk.music.player.service.PlayerService;
import ru.pusk.music.player.dto.AddPlayerResponse;
import ru.pusk.music.player.dto.PlayerChangeDto;

@RestController
@RequestMapping("/api/v1/music/player")
@Tag(
    name = "Плееры",
    description = "API для управления плеерами"
)
public class PlayerHttpApi {

  private final PlayerService playerService;

  public PlayerHttpApi(PlayerService playerService) {
    this.playerService = playerService;
  }



  @PostMapping("/add")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Добавить новый плеер"
  )
  public AddPlayerResponse addPlayer(
      @RequestBody PlayerChangeDto dto,
      @RequestParam(required = false) Long playerUserId,
      Authentication authentication) {
    Long userId = playerUserId == null ? Long.parseLong(authentication.getName()) : playerUserId;
    return playerService.addPlayer(
        userId,
        Long.parseLong(authentication.getName()),
        dto,
        Authorities.getFrom(authentication)
    );
  }


  @PostMapping("/{userId}/{playerId}/remove")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Удалить плеер у пользователя по ID плеера"
  )
  public void removePlayer(@PathVariable Long userId, @PathVariable Long playerId,
      Authentication authentication) {
    playerService.removePlayer(playerId, userId, Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication));
  }


  @GetMapping("/{playerId}")
  @PreAuthorize("isAuthenticated()")
  public PlayerResponse getById(@PathVariable Long playerId, Authentication authentication) {
    return playerService.getById(playerId, Long.valueOf(authentication.getName()),
        Authorities.getFrom(authentication));
  }

}
