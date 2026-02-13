package ru.pusk.api.http.personalinfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.personalinfo.dto.PersonalInfoDto;
import ru.pusk.personalinfo.service.PersonalInfoService;

@RestController
@RequestMapping("/api/v1/personal-info")
@PreAuthorize("isAuthenticated()")
@Tag(
    name = "Персональная информация",
    description = "API для управления персональной информацией пользователя"
)
public class PersonalInfoHttpApi {

  private final PersonalInfoService personalInfoService;

  public PersonalInfoHttpApi(PersonalInfoService personalInfoService) {
    this.personalInfoService = personalInfoService;
  }

  @GetMapping("/{userId}")
  @Operation(
      summary = "Получить персональную информацию пользователя по его ID"
  )
  public PersonalInfoDto getInfo(
      @PathVariable Long userId,
      Authentication authentication
  ) {
    return personalInfoService.getInfo(
        userId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{userId}/change")
  @Operation(
      summary = "Добавить/изменить персональную информацию пользователя по его ID"
  )
  public void changeInfo(
      @RequestBody PersonalInfoDto command,
      @PathVariable Long userId,
      Authentication authentication
  ) {
    personalInfoService.changeInfo(
        command,
        userId,
        Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

}
