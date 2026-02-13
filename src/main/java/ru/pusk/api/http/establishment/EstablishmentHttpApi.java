package ru.pusk.api.http.establishment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.establishment.dto.EstablishmentAddResponse;
import ru.pusk.establishment.dto.EstablishmentRequestDto;
import ru.pusk.establishment.dto.EstablishmentResponseDto;
import ru.pusk.establishment.service.EstablishmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/establishment")
@RequiredArgsConstructor
@Tag(name = "Объекты", description = "API для управления объектами")
public class EstablishmentHttpApi {

  private final EstablishmentService establishmentService;

  @GetMapping("/{userId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить список всех объектов пользователя"
  )
  public List<EstablishmentResponseDto> getEstablishments(@PathVariable Long userId,
      @RequestParam(required = false, defaultValue = "") String query,
      Authentication authentication) {
    return establishmentService.getEstablishments(
        query,
        userId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @GetMapping("/{userId}/{establishmentId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить объект пользователя"
  )
  public EstablishmentResponseDto getEstablishment(@PathVariable Long userId,
      @PathVariable Long establishmentId,
      Authentication authentication) {
    return establishmentService.getEstablishment(
        userId,
        establishmentId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping(value = "/{userId}/add")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Добавить новых объект пользователя (или изменить текущий)"
  )
  public EstablishmentAddResponse addEstablishments(@PathVariable Long userId,
      @RequestBody EstablishmentRequestDto request,
      Authentication authentication
  ) {
    return establishmentService.addEstablishment(request, userId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping(value = "/{userId}/{establishmentId}/delete")
  @PreAuthorize("isAuthenticated()")
  @Operation(
          summary = "Удалить объект по ID"
  )
  public void deleteEstablishment(@PathVariable Long userId, @PathVariable Long establishmentId,
                                  Authentication authentication) {
    establishmentService.deleteEstablishment(userId, Long.parseLong(authentication.getName()), establishmentId,
            Authorities.getFrom(authentication));
  }


}
