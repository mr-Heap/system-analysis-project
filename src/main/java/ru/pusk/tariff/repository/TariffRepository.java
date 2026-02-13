package ru.pusk.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.tariff.entity.Tariff;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

  List<Tariff> findAllByActiveIsTrue();
}
