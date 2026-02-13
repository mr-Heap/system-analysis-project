package ru.pusk.personalinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.auth.data.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

}
