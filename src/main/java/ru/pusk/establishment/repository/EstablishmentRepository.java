package ru.pusk.establishment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.personalinfo.enums.UserLegalType;

import java.util.List;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {


  @Query("SELECT e FROM Establishment e LEFT JOIN e.subscription s " +
      "WHERE e.userId = :userId AND " +
      "LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
      "(s IS NULL OR s.info.type = :type)")
  Page<Establishment> findByUserAndNameAndSubscriptionType(
      @Param("userId") Long userId,
      @Param("query") String query,
      @Param("type") UserLegalType type,
      Pageable pageable
  );

  @Query("SELECT e FROM Establishment e LEFT JOIN e.subscription s " +
      "WHERE e.userId = :userId AND " +
      "LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
      "(s IS NULL OR s.info.type = :type)")
  List<Establishment> findByUserAndNameAndSubscriptionType(
      @Param("userId") Long userId,
      @Param("query") String query,
      @Param("type") UserLegalType type
  );
}
