package ru.pusk.api.http.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pusk.auth.command.PhoneSendCodeCommand;
import ru.pusk.auth.command.PhoneVerifyCodeCommand;
import ru.pusk.auth.data.Tokens;
import ru.pusk.auth.response.PhoneSendCodeResponse;
import ru.pusk.auth.service.AuthPhoneService;

@RestController
@RequestMapping("/api/v1/auth/phone")
public class AuthPhoneHttpApi {

    private final AuthPhoneService authPhoneService;

    public AuthPhoneHttpApi(AuthPhoneService authPhoneService) {
        this.authPhoneService = authPhoneService;
    }

    @PostMapping("/send-code")
    public PhoneSendCodeResponse sendCode(@RequestBody PhoneSendCodeCommand command) {
        return authPhoneService.sendCode(command);
    }
    @PostMapping("/verify-code")
    public Tokens verifyCode(@RequestBody PhoneVerifyCodeCommand command) {
        return authPhoneService.verifyCode(command);
    }


}
