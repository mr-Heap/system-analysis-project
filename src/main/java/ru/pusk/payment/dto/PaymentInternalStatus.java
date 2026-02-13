package ru.pusk.payment.dto;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentInternalStatus {
  NEW("new"),
  PROCESSING("processing"),
  DONE("done"),
  CANCEL("cancel"),
  WAIT_FOR_WRITE_OFF_DATE("wait-for-write-off-date"),
  WAIT_FOR_MANAGER_APPROVED("wait-for-manager-approved")
  ;
  @JsonValue
  private final String value;
}
