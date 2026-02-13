package ru.pusk.payment.dto;

public record PaymentStatusDto(Long subId, PaymentInternalStatus status, String message) {
}
