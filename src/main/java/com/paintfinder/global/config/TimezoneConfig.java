package com.paintfinder.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@Configuration
@EnableJpaAuditing
@Slf4j
public class TimezoneConfig {
    @Bean
    public CommandLineRunner initTimeZone() {
        return args -> {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
            log.info("✅ TimeZone Set to: {}", TimeZone.getDefault().getID());
        };
    }
}