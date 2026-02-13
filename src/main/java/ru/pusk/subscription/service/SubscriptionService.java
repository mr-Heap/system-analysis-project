package ru.pusk.subscription.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.BadRequestError;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.establishment.dto.SubscriptionPaymentInfoDto;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.establishment.repository.EstablishmentRepository;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.legalinfo.repository.OrganizationInfoRepository;
import ru.pusk.payment.dto.PaymentStatusDto;
import ru.pusk.payment.dto.PaymentInternalStatus;
import ru.pusk.subscription.dto.SubscriptionOrganizationRequestDto;
import ru.pusk.subscription.dto.SubscriptionRequestDto;
import ru.pusk.subscription.dto.SubscriptionTariffRequestDto;
import ru.pusk.subscription.entity.Subscription;
import ru.pusk.subscription.repository.SubscriptionRepository;
import ru.pusk.tariff.entity.Tariff;
import ru.pusk.tariff.entity.TariffFeature;
import ru.pusk.tariff.entity.TariffSubFeature;
import ru.pusk.tariff.repository.TariffRepository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final TariffRepository tariffRepository;
  private final EstablishmentRepository establishmentRepository;
  private final OrganizationInfoRepository organizationInfoRepository;


  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public PaymentStatusDto addSubscription(Long userId, Long currentUserId, Long establishmentId,
      SubscriptionRequestDto subscriptionDto, Set<Authority> authorities
  ) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    Establishment establishment = establishmentRepository.findById(establishmentId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    Subscription subscription;
    if (subscriptionDto.getSubId() == null) {
      subscription = Subscription.builder()
          .createdAt(Timestamp.valueOf(now))
          .startDate(Timestamp.valueOf(now))
          .endDate(Timestamp.valueOf(now))
          .establishment(establishment)
          .active(false)
          .userId(establishment.getUserId())
          .items(new ArrayList<>())
          .build();
    } else {
      subscription = subscriptionRepository.findById(subscriptionDto.getSubId())
          .orElseThrow(() -> new ServiceException(new NotFoundError()));
    }
    changeSubscriptionTariff(subscription, subscriptionDto.getTariffInfo());
    changeSubscriptionOrganization(subscription, subscriptionDto.getOrganizationInfo());
    changeSubscriptionPayment(subscription, subscriptionDto.getPaymentInfo());
    var subID = subscriptionRepository.save(subscription).getId();
    return new PaymentStatusDto(subID, PaymentInternalStatus.DONE, "success");
  }

  private void changeSubscriptionTariff(Subscription subscription,
      SubscriptionTariffRequestDto dto) {
    if (dto == null) {
      return;
    }
    Tariff tariff = tariffRepository.findById(dto.getTariffId())
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (!tariff.getActive()) {
      throw new ServiceException(
          new BadRequestError("Couldn't add inactive tariff to establishment")
      );
    }
    if (dto.getItemIds() == null || dto.getItemIds().isEmpty()) {
      throw new ServiceException(
          new BadRequestError("Couldn't add establishment with empty subfeatures")
      );
    }
    subscription.setTariff(tariff);
    if (subscription.getItems() == null) {
      subscription.setItems(new ArrayList<>());
    }
    subscription.getItems().clear();
    Map<Long, TariffSubFeature> items = tariff.getFeatures()
        .stream()
        .map(TariffFeature::getItems)
        .flatMap(Set::stream)
        .collect(Collectors.toMap(TariffSubFeature::getId, Function.identity()));
    subscription.setPrice(
        tariff.getPrice() +
            subscription.getItems()
                .stream()
                .mapToDouble(TariffSubFeature::getPrice)
                .sum()
    );
    for (Long id : dto.getItemIds()) {
      if (items.containsKey(id)) {
        subscription.getItems().add(items.get(id));
      } else {
        throw new ServiceException(
            new BadRequestError(
                String.format(
                    "Couldn't add item with id %s, that doesn't belongs tariff with id %s",
                    id, tariff.getId()
                )
            )
        );
      }
    }
    if (subscription.getTariff() == null) {
      if (subscription.getInfo() == null) {
        throw new ServiceException(
            new BadRequestError("Forbidden to create subscription without bound tariff")
        );
      }
    }
  }


  private void changeSubscriptionOrganization(
      Subscription subscription,
      SubscriptionOrganizationRequestDto organizationInfo
  ) {
    if (organizationInfo == null) {
      if (subscription.getInfo() == null) {
        throw new ServiceException(
            new BadRequestError("Forbidden to create subscription without organization")
        );
      }
      return;
    }
    //TODO: Здесь можно добавить не свою организацию. Проверить и исправить!
    Long organizationId = organizationInfo.getOrganizationId();
    OrganizationInfo info = organizationInfoRepository.findById(organizationId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    subscription.setInfo(info);
  }


  private void changeSubscriptionPayment(Subscription subscription,
      SubscriptionPaymentInfoDto paymentInfo) {
    if (paymentInfo == null || (
        (paymentInfo.getAccountId() == null || paymentInfo.getAccountId() == 0) && (
            paymentInfo.getCardId() == null || paymentInfo.getCardId() == 0))) {
      throw new ServiceException("Couldn't change payment. No account or card");
    }
    if (paymentInfo.getAccountId() == null || paymentInfo.getAccountId() == 0) {
      subscription.setPaymentAccountId(null);
    } else {
      subscription.setPaymentAccountId(paymentInfo.getAccountId());
      subscription.setPaymentCardId(null);
    }
    if (paymentInfo.getCardId() == null || paymentInfo.getCardId() == 0) {
      subscription.setPaymentCardId(null);
    } else {
      subscription.setPaymentCardId(paymentInfo.getCardId());
      subscription.setPaymentAccountId(null);
    }
    if (subscription.getPaymentAccountId() == null && subscription.getPaymentCardId() == null) {
      throw new ServiceException(
          new BadRequestError(
              "Forbidden to create subscription without any payment type. Must be chosen payment car or account"
          )
      );
    }
  }
}
