package ru.pusk.api.http.legalinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.legalinfo.dto.LegalInfoDto;
import ru.pusk.legalinfo.service.LegalInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/legal-info")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class LegalInfoHttpApi {

  private final LegalInfoService legalInfoService;

  @GetMapping("/{userId}/ul")
  public List<LegalInfoDto> getULInfo(@PathVariable Long userId, Authentication authentication) {
    return legalInfoService.getLegalInfoList(
        userId, Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @GetMapping("/{userId}/ip")
  public List<LegalInfoDto> getIPInfo(@PathVariable Long userId, Authentication authentication) {
    return legalInfoService.getIPList(
        userId, Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{userId}/ip/add")
  public void addIPInfo(
      @RequestBody LegalInfoDto dto,
      @PathVariable Long userId,
      Authentication authentication
  ) {
    legalInfoService.addLegalInfo(dto,
        userId, Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{userId}/ul/add")
  public void addULInfo(
      @RequestBody LegalInfoDto dto,
      @PathVariable Long userId,
      Authentication authentication
  ) {
    legalInfoService.addLegalInfo(dto,
        userId, Long.parseLong(authentication.getName()),
        Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{userId}/ip/delete")
  public void deleteIPInfo(
          @RequestBody LegalInfoDto dto,
          @PathVariable Long userId,
          Authentication authentication
  ) {
    legalInfoService.deleteLegalInfo(dto,
            userId, Long.parseLong(authentication.getName()),
            Authorities.getFrom(authentication)
    );
  }

  @PostMapping("/{userId}/ul/delete")
  public void deleteULInfo(
          @RequestBody LegalInfoDto dto,
          @PathVariable Long userId,
          Authentication authentication
  ) {
    legalInfoService.deleteLegalInfo(dto,
            userId, Long.parseLong(authentication.getName()),
            Authorities.getFrom(authentication)
    );
  }


}
