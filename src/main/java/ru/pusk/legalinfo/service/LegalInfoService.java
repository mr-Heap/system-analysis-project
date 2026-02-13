package ru.pusk.legalinfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.Authority;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.FieldIncorrectError;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.legalinfo.dto.*;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.legalinfo.error.NotUniqueIPDataError;
import ru.pusk.legalinfo.mapper.LegalInfoMapper;
import ru.pusk.legalinfo.repository.OrganizationInfoRepository;
import ru.pusk.payment.entity.PaymentAccount;
import ru.pusk.payment.dto.PaymentAccountDto;
import ru.pusk.payment.repository.PaymentAccountRepository;
import ru.pusk.personalinfo.enums.UserLegalType;
import ru.pusk.personalinfo.repository.IndividualInfoRepository;
import ru.pusk.subscription.repository.SubscriptionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static ru.pusk.common.CommonUtils.concat;

@Service
@Slf4j
@RequiredArgsConstructor
public class LegalInfoService {

  private final OrganizationInfoRepository organizationInfoRepository;
  private final IndividualInfoRepository individualInfoRepository;
  private final PaymentAccountRepository paymentAccountRepository;
  private final SubscriptionRepository subscriptionRepository;

  private final LegalInfoMapper legalInfoMapper;
  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
      + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public List<LegalInfoDto> getLegalInfoList(Long userId, long currentUserId,
      Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    return legalInfoMapper.toLegalInfoDtoList(
        organizationInfoRepository.findAllByUserIdAndType(userId, UserLegalType.LEGAL_ENTITY)
    );
  }


  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
      + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public List<LegalInfoDto> getIPList(Long userId, long currentUserId,
      Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    return legalInfoMapper.toLegalInfoDtoList(
        organizationInfoRepository.findAllByUserIdAndType(userId, UserLegalType.IP)
    );
  }


  @Transactional
  @PreAuthorize("hasAnyAuthority("
          + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
          + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
          + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
          + ")"
  )
  public void deleteLegalInfo(LegalInfoDto dto, Long userId, long currentUserId,
                           Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    organizationInfoRepository.deleteById(dto.getId());
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
      + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  public void addLegalInfo(LegalInfoDto dto, Long userId, long currentUserId,
      Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    var individualInfo = individualInfoRepository.findByUserId(userId)
        .orElseThrow(() -> new ServiceException(new NotFoundError()));
    List<OrganizationInfo> infos = organizationInfoRepository.findAllByUserIdAndType(
        userId, individualInfo.getType()
    );
    if (dto.getId() == null) {
      checkRequiredFields(dto, individualInfo.getType());
    }
    if (!infos.isEmpty() &&
        individualInfo.getType() == UserLegalType.IP &&
        !infos.getFirst().getId().equals(dto.getId())
    ) {
      throw new ServiceException(
          new NotUniqueIPDataError("Couldn't add more than one legal data to ip")
      );
    }
    for (var info : infos) {
      if (Objects.equals(dto.getInn(), info.getInn()) &&
          Objects.equals(dto.getKpp(), info.getKpp()) &&
          Objects.equals(dto.getOgrn(), info.getOgrn()) &&
          !info.getId()
              .equals(dto.getId()) // Мы добавляем новое юр лицо с такими же параметрами - плохо
      ) {
        return;
      }
    }

    OrganizationInfo info = dto.getId() == null
        ? OrganizationInfo.builder().userId(userId).type(individualInfo.getType()).build()
        : organizationInfoRepository.findById(dto.getId())
            .orElseThrow(() -> new ServiceException(new NotFoundError()));
    info.setInn(concat(dto.getInn(), info.getInn()));
    info.setCompanyName(concat(dto.getCompanyName(), info.getCompanyName()));
    info.setFio(concat(dto.getFio(), info.getFio()));
    info.setAddress(concat(dto.getAddress(), info.getAddress()));
    info.setEmail(concat(dto.getEmail(), info.getEmail()));
    info.setKpp(concat(dto.getKpp(), info.getKpp()));
    info.setOgrn(concat(dto.getOgrn(), info.getOgrn()));
    OrganizationInfo updatedInfo = organizationInfoRepository.save(info);
    if (dto.getAccounts() != null && !dto.getAccounts().isEmpty()) {
      addAccount(dto.getAccounts().getFirst(), updatedInfo);
    }
  }


  private void addAccount(PaymentAccountDto accountDto, OrganizationInfo info) {
    PaymentAccount account = accountDto.getId() == null
        ? PaymentAccount.builder().build()
        : paymentAccountRepository.findById(accountDto.getId())
            .orElseThrow(() -> new ServiceException(new NotFoundError()));
    if (accountDto.getAccountNumber() != null) {
      account.setAccountNumber(accountDto.getAccountNumber());
    }
    if (accountDto.getBankBranchName() != null) {
      account.setBankBranchName(accountDto.getBankBranchName());
    }
    if (accountDto.getBankBranchAddress() != null) {
      account.setBankBranchAddress(accountDto.getBankBranchAddress());
    }
    if (accountDto.getBankBik() != null) {
      account.setBankBik(accountDto.getBankBik());
    }
    if (accountDto.getCorrespondentNumber() != null) {
      account.setCorrespondentNumber(accountDto.getCorrespondentNumber());
    }
    account.setOrganizationInfo(info);
    account.setOwnerUserId(info.getUserId());
    paymentAccountRepository.save(account);
  }

  private void checkRequiredFields(LegalInfoDto dto, UserLegalType type) {
    if (dto == null) {
      throw new ServiceException(new FieldIncorrectError("DTO"));
    }
    final Stream<Object> values;
    switch (type) {
      case IP -> values = Stream.of(
          dto.getInn(),
          dto.getOgrn(),
          dto.getFio(),
          dto.getAddress(),
          dto.getEmail()
      );
      case LEGAL_ENTITY -> values = Stream.of(
          dto.getInn(),
          dto.getKpp(),
          dto.getOgrn(),
          dto.getCompanyName(),
          dto.getAddress()
      );
      default -> values = Stream.ofNullable(null);
    }
    if (values.anyMatch(Objects::isNull)) {
      throw new ServiceException(new FieldIncorrectError("Not enough required fields"));
    }
  }
}
