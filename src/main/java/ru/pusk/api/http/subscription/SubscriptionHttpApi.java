package ru.pusk.api.http.subscription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.pusk.common.Authorities;
import ru.pusk.payment.dto.PaymentStatusDto;
import ru.pusk.subscription.dto.SubscriptionRequestDto;
import ru.pusk.subscription.service.SubscriptionService;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
@Tag(name = "Подписки", description = "API для управления подписками")
public class SubscriptionHttpApi {

  private final SubscriptionService subscriptionService;


  @PostMapping("/{userId}/{establishmentId}/add")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Добавить подписку на объект"
  )
  public PaymentStatusDto addSubscription(
      @PathVariable Long userId,
      @PathVariable Long establishmentId,
      @RequestBody SubscriptionRequestDto subscriptionDto,
      Authentication authentication
  ) {
    return subscriptionService.addSubscription(
        userId,
        Long.parseLong(authentication.getName()),
        establishmentId,
        subscriptionDto,
        Authorities.getFrom(authentication)
    );
  }

}
