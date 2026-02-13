package ru.pusk.common;

import org.springframework.security.access.AccessDeniedException;
import ru.pusk.auth.data.Authority;

import java.util.Set;

public class AccessService {

  //check that user is call some method on its own or it's an admin
  public static void checkAccess(Long userId, Long currentUserId, Set<Authority> authorities) {
    if (!userId.equals(currentUserId) && authorities.stream()
        .noneMatch(a -> a == Authority.ROLE_ADMIN)
    ) {
      throw new AccessDeniedException("Access Denied");
    }
  }

  public static void checkAdminAccess(Set<Authority> authorities) {
    if (authorities.stream().noneMatch(a -> a == Authority.ROLE_ADMIN)) {
      throw new AccessDeniedException("Access Denied");
    }
  }

  public static boolean isAdmin(Set<Authority> authorities) {
   return authorities.stream().anyMatch(a -> a == Authority.ROLE_ADMIN);
  }
}
