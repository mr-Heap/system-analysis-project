package ru.pusk.auth.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;
import ru.pusk.auth.data.Authority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class JwtAuthService {

    private final JwtEncoder jwtEncoder;
    @Getter
    private final JwtDecoder jwtDecoder;
    private final MacAlgorithm macAlgorithm;
    private final String authoritiesClaimName;
    @Getter
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public JwtAuthService(
            @Value("${pusk.auth.jwt.secret-key}") String authJwtSecretKey,
            @Value("${pusk.auth.jwt.mac-algorithm}") MacAlgorithm macAlgorithm,
            @Value("${pusk.auth.jwt.algorithm}") String algorithm,
            @Value("${pusk.auth.jwt.authorities-claim-name}") String authoritiesClaimName) {
        this.authoritiesClaimName = authoritiesClaimName;
        this.macAlgorithm = macAlgorithm;
        SecretKey key = new SecretKeySpec(authJwtSecretKey.getBytes(), algorithm);
        var immutableSecret = new ImmutableSecret<>(key);
        this.jwtEncoder = buildJwtEncoder(immutableSecret);
        this.jwtDecoder = buildJwtDecoder(key);
        this.jwtAuthenticationConverter = buildJwtAuthenticationConverter(authoritiesClaimName);
    }

    private JwtEncoder buildJwtEncoder(ImmutableSecret<SecurityContext> immutableSecret) {
        return new NimbusJwtEncoder(immutableSecret);
    }
    private JwtDecoder buildJwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(macAlgorithm)
                .build();
    }

    private JwtAuthenticationConverter buildJwtAuthenticationConverter(String authoritiesClaimName) {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(authoritiesClaimName);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    public String encode(String userId, Set<Authority> authorities) {
        var claims = JwtClaimsSet.builder()
                .subject(userId)
                .expiresAt(Instant.now().plus(Integer.MAX_VALUE, ChronoUnit.DAYS))
                .claim(authoritiesClaimName,
                        authorities.stream()
                                .map(Authority::getValue)
                                .collect(Collectors.toSet()))
                .build();
        var jwsHeader = JwsHeader.with(macAlgorithm).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
