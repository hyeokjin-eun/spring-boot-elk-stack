package com.example.logging.logging.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {

    public void getLogging() {
        log.info("Logging Service in getLogging()");
    }

    public void postLogging() {
        log.info("Logging Service in postLogging()");
    }
}
