package com.example.logging.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanComponent {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
