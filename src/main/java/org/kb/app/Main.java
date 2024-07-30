package org.kb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@EnableWebSecurity
@SpringBootApplication
public class Main {
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}