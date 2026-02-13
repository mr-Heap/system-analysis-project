package ru.pusk.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.client.FakeSmsClient;
import ru.pusk.auth.command.PhoneSendCodeCommand;
import ru.pusk.auth.command.PhoneVerifyCodeCommand;
import ru.pusk.auth.data.AuthPhone;
import ru.pusk.auth.data.PhoneCode;
import ru.pusk.auth.data.Tokens;
import ru.pusk.auth.error.WrongPhoneCodeError;
import ru.pusk.auth.repository.AuthPhoneRepository;
import ru.pusk.auth.repository.PhoneCodeRepository;
import ru.pusk.auth.repository.UserAuthorityRepository;
import ru.pusk.auth.response.PhoneSendCodeResponse;
import ru.pusk.common.ServiceException;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class AuthPhoneService {

  private final static long PHONE_CODE_LIFETIME_IN_SECONDS = 60 * 3;
  private final static long MAX_ATTEMPT_PHONE_CODE_VERIFICATION = 3;

  private final FakeSmsClient smsClient;
  private final PhoneCodeRepository phoneCodeRepository;
  private final AuthPhoneRepository authPhoneRepository;
  private final SignInUserService signInUserService;

  private final UserAuthorityRepository userAuthorityRepository;

  private final boolean isSmsClientFake;

  public AuthPhoneService(FakeSmsClient smsClient,
      PhoneCodeRepository phoneCodeRepository,
      AuthPhoneRepository authPhoneRepository,
      SignInUserService signInUserService, UserAuthorityRepository userAuthorityRepository,
      @Value("${pusk.auth.sms-client.fake}") boolean isSmsClientFake) {
    this.smsClient = smsClient;
    this.phoneCodeRepository = phoneCodeRepository;
    this.authPhoneRepository = authPhoneRepository;
    this.signInUserService = signInUserService;
    this.userAuthorityRepository = userAuthorityRepository;
    this.isSmsClientFake = isSmsClientFake;
  }

  @PreAuthorize("permitAll()")
  @Transactional
  public PhoneSendCodeResponse sendCode(PhoneSendCodeCommand command) {
    var code = isSmsClientFake ? "111111" : DigitCodeGenerateService.generateCodeDigits(6);
    var expireAt = LocalDateTime.now(Clock.systemUTC())
        .plusSeconds(PHONE_CODE_LIFETIME_IN_SECONDS);
    Long userId = authPhoneRepository.findByPhone(command.phone())
        .map(AuthPhone::getUserId)
        .orElse(-1L);
    phoneCodeRepository.deleteAllByPhone(command.phone());
    var phoneCode = PhoneCode.builder()
        .code(code)
        .attemptNumber(0)
        .expireAt(expireAt)
        .phone(command.phone())
        .build();
    phoneCodeRepository.save(phoneCode);
    smsClient.sendSms(command.phone(), "Код для входа в PUSK: " + code);
    String authority = userAuthorityRepository.findAllByUserId(userId)
        .stream()
        .findFirst()
        .map(a -> a.getAuthority().getValue())
        .orElse("none");

    return new PhoneSendCodeResponse(phoneCode.getExpireAt(), authority);
  }


  @PreAuthorize("permitAll()")
  @Transactional
  public Tokens verifyCode(PhoneVerifyCodeCommand command) {
    var phoneCode = phoneCodeRepository.findByPhone(command.phone())
        .orElseThrow(() -> new ServiceException(new WrongPhoneCodeError()));
    if (phoneCode.getExpireAt().isBefore(LocalDateTime.now(Clock.systemUTC()))
        || phoneCode.getAttemptNumber() >= MAX_ATTEMPT_PHONE_CODE_VERIFICATION) {
      phoneCodeRepository.delete(phoneCode);
      throw new ServiceException(new WrongPhoneCodeError());
    }
    if (!phoneCode.getCode().equals(command.code())) {
      phoneCode.setAttemptNumber(phoneCode.getAttemptNumber() + 1);
      phoneCodeRepository.save(phoneCode);
      throw new ServiceException(new WrongPhoneCodeError());
    }
    phoneCodeRepository.delete(phoneCode);
    var authPhoneOpt = authPhoneRepository.findByPhone(command.phone());
    Long userId;
    if (authPhoneOpt.isPresent()) {
      userId = authPhoneOpt.get().getUserId();
      var tokens = signInUserService.signIn(userId);
      return new Tokens(tokens.token(), tokens.refreshToken());
    } else {
      var userIdAndToken = signInUserService.signUp();
      var authPhone = AuthPhone.builder()
          .phone(command.phone())
          .userId(userIdAndToken.userId())
          .build();
      authPhoneRepository.save(authPhone);
      return new Tokens(
          userIdAndToken.tokens().token(),
          userIdAndToken.tokens().refreshToken()
      );
    }
  }
}
