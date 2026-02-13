package ru.pusk.auth.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name = "user_authority")
@Entity(name = "UserAuthority")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    private Long userId;
}
