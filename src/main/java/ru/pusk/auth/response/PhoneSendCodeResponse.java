package ru.pusk.auth.response;

import java.time.LocalDateTime;

public record PhoneSendCodeResponse(LocalDateTime expireAt, String authority) {
}
