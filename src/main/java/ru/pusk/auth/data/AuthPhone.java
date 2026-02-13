package ru.pusk.auth.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name = "auth_phone")
@Entity(name = "AuthPhone")
@NoArgsConstructor
@AllArgsConstructor
public class AuthPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    @Column(name = "user_id")
    private Long userId;
}
