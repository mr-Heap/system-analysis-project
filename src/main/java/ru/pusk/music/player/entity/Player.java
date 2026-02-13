package ru.pusk.music.player.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.establishment.entity.Establishment;

@Getter
@Setter
@Builder
@Entity(name = "Player")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"establishment"})
@EqualsAndHashCode(exclude = {"establishment"})
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_user_id")
    private Long ownerUserId;
    @Enumerated(EnumType.STRING)
    private ClientType type;
    private String name;
    private String address;
    private String serialBoxNumber;
    private String version;
    @OneToOne(fetch = FetchType.LAZY)
    private Establishment establishment;

}
