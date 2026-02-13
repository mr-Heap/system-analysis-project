package ru.pusk.api.http.tariff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.tariff.dto.TariffDto;
import ru.pusk.tariff.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariff")
@RequiredArgsConstructor
@Tag(name = "Тарифы", description = "API для управления тарифами")
public class TariffHttpApi {

  private final TariffService tariffService;

  @GetMapping()
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить список всех активных тарифов(для любого пользователя)"
  )
  public List<TariffDto> getTariffs() {
    return tariffService.getTariffs();
  }

  @GetMapping("/{tariffId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить тариф по ID"
  )
  public TariffDto getTariffById(@PathVariable Long tariffId) {
    return tariffService.getTariffById(tariffId);
  }


  @GetMapping("/{userId}/{establishmentId}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Получить информаацию по тарифу конкретного объекта"
  )
  public TariffDto getEstablishmentTariff(
      @PathVariable Long userId,
      @PathVariable Long establishmentId,
      Authentication authentication
  ) {
    return tariffService.getEstablishmentTariff(userId, Long.parseLong(authentication.getName()),
        establishmentId,
        Authorities.getFrom(authentication));
  }

}
