package ru.pusk.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.auth.data.AuthPhone;

import java.util.Optional;

@Repository
public interface AuthPhoneRepository extends JpaRepository<AuthPhone, Long> {

  Optional<AuthPhone> findByPhone(String phone);

  Optional<AuthPhone> findByUserId(Long userId);
}
