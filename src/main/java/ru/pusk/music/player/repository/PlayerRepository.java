package ru.pusk.music.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.music.player.entity.ClientType;
import ru.pusk.music.player.entity.Player;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

  Optional<Player> findByEstablishmentId(Long establishmentId);
  Optional<Player> findByOwnerUserIdAndSerialBoxNumberAndType(Long userId, String serialBoxNumber, ClientType type);
}
