package ru.pusk.auth.client;

import org.springframework.stereotype.Service;
@Service("smsClient")
public class FakeSmsClient {

    public boolean sendSms(String phone, String text) {
        return true;
    }
}
