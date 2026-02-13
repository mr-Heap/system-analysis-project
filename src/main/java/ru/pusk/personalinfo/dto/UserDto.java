package ru.pusk.personalinfo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.pusk.auth.data.Authority;

import java.sql.Timestamp;


public interface UserDto {
  Long getId();
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
  Timestamp getRegistrationDate();
  String getTariffs();
  String getSubscriptionStatus();
  String getLogin();
  String getFio();
  String getStatus();
  Authority getRole();
}
