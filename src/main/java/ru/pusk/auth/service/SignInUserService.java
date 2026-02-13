package ru.pusk.auth.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.pusk.auth.data.*;
import ru.pusk.auth.repository.AuthUserRepository;
import ru.pusk.auth.repository.TokenRepository;
import ru.pusk.auth.repository.UserAuthorityRepository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SignInUserService {

  private final AuthUserRepository authUserRepository;
  private final UserAuthorityRepository userAuthorityRepository;
  private final JwtAuthService jwtAuthService;
  private final TokenRepository tokenRepository;

  public SignInUserService(AuthUserRepository authUserRepository,
      UserAuthorityRepository userAuthorityRepository,
      JwtAuthService jwtAuthService,
      TokenRepository tokenRepository) {
    this.authUserRepository = authUserRepository;
    this.userAuthorityRepository = userAuthorityRepository;
    this.jwtAuthService = jwtAuthService;
    this.tokenRepository = tokenRepository;
  }

  public Tokens signIn(Long userId) {
    var token = createRefreshToken(userId);
    tokenRepository.save(token);
    var userAuthorities = userAuthorityRepository.findAllByUserId(userId);
    return new Tokens(
        jwtAuthService.encode(userId.toString(), getAuthorities(userAuthorities)),
        token.getRefreshToken()
    );
  }

  public UserIdAndToken signUp() {
    var user = createNewUser();
    var authority = UserAuthority.builder()
        .authority(Authority.ROLE_CLIENT)
        .userId(user.getUserId())
        .build();
    userAuthorityRepository.save(authority);
    var token = createRefreshToken(user.getUserId());
    tokenRepository.save(token);
    return new UserIdAndToken(
        user.getUserId(),
        new Tokens(
            jwtAuthService.encode(
                user.getUserId().toString(),
                getAuthorities(userAuthorityRepository.findAllByUserId(user.getUserId()))
            ),
            token.getRefreshToken()
        )
    );
  }


  private @NonNull Users createNewUser() {
    return authUserRepository.save(
        Users.builder()
            .registeredAt(Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC())))
            .build()
    );
  }

  private Token createRefreshToken(Long userId) {
    return Token.builder()
        .userId(userId)
        .expireAt(LocalDateTime.now().plusMonths(1))
        .refreshToken(String.valueOf(UUID.randomUUID()))
        .build();
  }

  private Set<Authority> getAuthorities(
      Set<UserAuthority> userAuthorities) {
    return userAuthorities.stream()
        .map(UserAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  public record UserIdAndToken(Long userId, Tokens tokens) {

  }

}
