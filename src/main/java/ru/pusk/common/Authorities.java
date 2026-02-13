package ru.pusk.common;

import org.springframework.security.core.Authentication;
import ru.pusk.auth.data.Authority;

import java.util.Set;
import java.util.stream.Collectors;

public class Authorities {
    public static Set<Authority> getFrom(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(grantedAuthority ->
                        Authority.fromValue(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
