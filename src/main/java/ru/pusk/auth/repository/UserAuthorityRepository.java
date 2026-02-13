package ru.pusk.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pusk.auth.data.Authority;
import ru.pusk.auth.data.UserAuthority;

import java.util.Set;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

  Set<UserAuthority> findAllByUserId(Long userId);

  @Modifying
  @Query("update UserAuthority ua set ua.authority = :authority where ua.userId = :userId")
  void updateRole(@Param("userId") Long userId, @Param("authority") Authority authority);
}
