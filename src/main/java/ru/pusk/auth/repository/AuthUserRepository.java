package ru.pusk.auth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pusk.auth.data.Users;

@Repository
public interface AuthUserRepository extends JpaRepository<Users, Long> {

}
