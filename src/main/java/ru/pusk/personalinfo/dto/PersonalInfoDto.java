package ru.pusk.personalinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import ru.pusk.auth.data.Authority;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoDto {
  private Long userId;
  private String imageUrl;
  private String lastName;
  private String firstName;
  private String patronymic;
  private String phone;
  private String email;
  private Authority role;
  private String type; // individual, legal
}
