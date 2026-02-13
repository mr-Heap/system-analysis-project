package ru.pusk.auth.command;

public record PhoneVerifyCodeCommand(String phone, String code) {
}
