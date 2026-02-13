package ru.pusk.api.http.fail;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fail")
@RequiredArgsConstructor
@Tag(name = "Ошибки", description = "API, которое всегда отдает ошибки")
public class FailHttpApi {

    @GetMapping("/bad-request")
    public ResponseEntity<String> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Это тестовая ошибка 400");
    }

    @GetMapping("/internal-error")
    public ResponseEntity<String> internalError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Это тестовая ошибка 500");
    }

    @GetMapping("/forbidden")
    public ResponseEntity<String> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Это тестовая ошибка 403");
    }

    @GetMapping("/custom/{code}")
    public ResponseEntity<String> customError(@PathVariable int code) {
        return ResponseEntity.status(code)
                .body("Это тестовая ошибка с кодом " + code);
    }
}