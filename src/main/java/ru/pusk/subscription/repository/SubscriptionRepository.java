package ru.pusk.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.establishment.entity.Establishment;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.subscription.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  Optional<Subscription> findFirstByEstablishment(Establishment establishment);
}
