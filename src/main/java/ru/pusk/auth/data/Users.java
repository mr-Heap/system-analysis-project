package ru.pusk.auth.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@Table(name = "users")
@Entity(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private Timestamp registeredAt;
}
