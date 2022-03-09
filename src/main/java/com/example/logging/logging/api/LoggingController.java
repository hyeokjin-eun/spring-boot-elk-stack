package com.example.logging.logging.api;

import com.example.logging.logging.application.LoggingService;
import com.example.logging.logging.dto.PostLoggingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoggingController {

    private final LoggingService loggingService;

    @GetMapping("/")
    public ResponseEntity<String> getLogging(@RequestParam String message) {
        log.info("Logging Controller in getLogging()");
        loggingService.getLogging();
        return ResponseEntity.ok()
                .body(message);
    }

    @PostMapping("/")
    public ResponseEntity<String> postLogging(@RequestBody PostLoggingRequestDto postLoggingRequestDto) {
        log.info("Logging Controller in postLogging()");
        loggingService.postLogging();
        return ResponseEntity.ok()
                .body(postLoggingRequestDto.toString());
    }

    @GetMapping("/exception")
    public ResponseEntity<String> exceptionLogging() {
        log.info("Logging Controller in exceptionLogging()");
        throw new RuntimeException();
    }
}
