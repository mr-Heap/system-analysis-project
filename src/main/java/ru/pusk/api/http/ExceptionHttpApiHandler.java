package ru.pusk.api.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.pusk.auth.error.*;
import ru.pusk.common.ServiceException;
import ru.pusk.common.error.BadRequestError;
import ru.pusk.common.error.FailedUploadFileError;
import ru.pusk.common.error.FieldIncorrectError;
import ru.pusk.common.error.NotFoundError;
import ru.pusk.legalinfo.error.NotUniqueIPDataError;


import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHttpApiHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
    log.debug("Access Denied", ex);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .build();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    if (e.getCause() != null && e.getCause().getCause() != null && e.getCause()
        .getCause() instanceof ServiceException serviceException) {
      return handleServiceException(serviceException);
    }
    log.error("Unexpected error! Change to expected", e);
    return ResponseEntity.internalServerError().build();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<?> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    return ResponseEntity
        .badRequest()
        .body(new ErrorResponse(Map.of(
            "type", "missing-request-parameter",
            "parameter", e.getParameterName()
        )));
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<?> handleServiceException(ServiceException e) {
    return switch (e.getError()) {
      case NotFoundError error -> ResponseEntity
          .notFound()
          .build();
      case FieldIncorrectError error -> {
        Map<String, String> result = new java.util.HashMap<>();
        result.put("type", "field-incorrect");
        result.put("field", error.fieldName());
        result.put("reason", error.reason());
        yield ResponseEntity
            .badRequest()
            .body(new ErrorResponse(result));
      }
      case WrongFileFormatError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of("type", "wrong-format-code")));
      case WrongPhoneCodeError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of(
              "type", "wrong-phone-code")
          ));
      case AccountDeletedError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of(
              "type", "account-deleted")
          ));
      case NoSuchAuthCodeError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of(
              "type", "no-such-auth-code")
          ));
      case CantSendSmsError error -> {
        log.warn("Cant send sms with error code " + error.errorCode());
        yield ResponseEntity
            .badRequest()
            .body(new ErrorResponse(Map.of(
                "type", "send-sms-fail")
            ));
      }
      case NotUniqueIPDataError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of(
              "type", "ip-data-not-unique",
              "reason", error.message()
          )));
      case FailedUploadFileError error -> ResponseEntity.internalServerError().build();

      case BadRequestError error -> ResponseEntity
          .badRequest()
          .body(new ErrorResponse(Map.of(
              "type", "bad-request",
              "reason", error.message()
          )));
      default -> {
        log.error("Unknown ServiceException error type");
        yield ResponseEntity.internalServerError()
            .body(
                new ErrorResponse(Map.of(
                    "reason", e.getError()
                ))
            );
      }
    };
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> handleServiceException(Throwable e) {
    log.error("Unknown exception error", e);
    return ResponseEntity.internalServerError().build();
  }

  private ResponseEntity<?> unauthorized() {
    return ResponseEntity.status(401).build();
  }
}
