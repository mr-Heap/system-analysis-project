package ru.pusk.auth.command;

import ru.pusk.common.ServiceException;
import ru.pusk.common.ValidateService;
import ru.pusk.common.error.FieldIncorrectError;

public record PhoneSendCodeCommand(String phone) {

  public PhoneSendCodeCommand {
    if (!ValidateService.isValidPhone(phone)) {
      throw new ServiceException(new FieldIncorrectError("phone"));
    }
  }
}
