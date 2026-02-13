package ru.pusk.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.payment.entity.PaymentAccount;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {

}
