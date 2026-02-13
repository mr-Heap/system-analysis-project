package ru.pusk.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.auth.data.PhoneCode;

import java.util.Optional;

@Repository
public interface PhoneCodeRepository extends JpaRepository<PhoneCode, Long> {

  Optional<PhoneCode> findByPhone(String phone);

  void deleteAllByPhone(String phone);
}
