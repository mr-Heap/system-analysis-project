package ru.pusk.personalinfo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.*;
import ru.pusk.auth.repository.AuthPhoneRepository;
import ru.pusk.auth.repository.UserAuthorityRepository;
import ru.pusk.common.AccessService;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.personalinfo.dto.PersonalInfoDto;
import ru.pusk.personalinfo.entity.IndividualInfo;
import ru.pusk.personalinfo.enums.UserLegalType;
import ru.pusk.personalinfo.mapper.PersonalInfoMapper;
import ru.pusk.personalinfo.repository.IndividualInfoRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class PersonalInfoService {

  private final Set<Authority> authoritiesAccessedToAllUserPersonalInfo =
      Set.of(Authority.ROLE_ADMIN, Authority.ROLE_CLIENT_MANAGER);
  private final AuthPhoneRepository authPhoneRepository;
  private final IndividualInfoRepository individualInfoRepository;
  private final UserAuthorityRepository userAuthorityRepository;
  private final PersonalInfoMapper personalInfoMapper;
  private static final int DEFAULT_USER_AMOUNT_BY_PAGE = 12;



  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
      + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  @Transactional
  public PersonalInfoDto getInfo(Long userId, Long currentUserId, Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    var user = getOrCreateIndividualInfo(userId);
    PersonalInfoDto info = personalInfoMapper.toPersonalInfoDto(user);
    Authority authority = userAuthorityRepository.findAllByUserId(userId)
        .stream()
        .findFirst()
        .orElseThrow(() -> new ServiceException(new NotFoundError()))
        .getAuthority();
    info.setRole(authority);

    if (user != null && user.getAvatarFileId() != null) {
      info.setImageUrl(
          String.format("/api/v1/personal-info/avatar-file/%s/%s", userId, user.getAvatarFileId()));
    }
    return info;
  }


  @PreAuthorize("hasAnyAuthority("
      + "T(ru.pusk.auth.data.Authority).SIGN_AGREEMENT.value,"
      + "T(ru.pusk.auth.data.Authority).EDIT_PERSONAL_INFO.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CONTENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_CLIENT_MANAGER.value,"
      + "T(ru.pusk.auth.data.Authority).ROLE_ADMIN.value"
      + ")"
  )
  @Transactional
  public void changeInfo(PersonalInfoDto dto, Long userId, Long currentUserId,
      Set<Authority> authorities) {
    AccessService.checkAccess(userId, currentUserId, authorities);
    IndividualInfo individualInfo = getOrCreateIndividualInfo(userId);
    String firstName = dto.getFirstName();
    String lastName = dto.getLastName();
    String patronymic = dto.getPatronymic();
    String email = dto.getEmail();
    String phone = dto.getPhone();
    String type = dto.getType();
    Authority role = dto.getRole();
    if (firstName != null && !firstName.isBlank()) {
      individualInfo.setFirstName(dto.getFirstName());
    }
    if (lastName != null && !lastName.isBlank()) {
      individualInfo.setLastName(lastName);
    }
    if (patronymic != null && !patronymic.isBlank()) {
      individualInfo.setPatronymic(patronymic);
    }
    if (email != null) {
      individualInfo.setContactEmail(email);
    }
    if (phone != null && !phone.isBlank()) {
      individualInfo.setPhone(phone);
    }
    if (type != null && !type.isBlank()) {
      individualInfo.setType(UserLegalType.fromString(type));
    }
    if (role != null && authorities.contains(Authority.ROLE_ADMIN)) {
      userAuthorityRepository.updateRole(userId, role);
    }


    individualInfoRepository.save(individualInfo);
  }

  private IndividualInfo getOrCreateIndividualInfo(Long userId) {
    var info = individualInfoRepository.findByUserId(userId);
    return info.orElseGet(
        () -> individualInfoRepository.save(
            IndividualInfo.builder()
                .userId(userId)
                .phone(
                    authPhoneRepository.findByUserId(userId)
                        .map(AuthPhone::getPhone)
                        .orElse(null)
                )
                .firstName("Пользователь №%s".formatted(userId))
                .type(UserLegalType.IP)
                .build()
        ));
  }
}
