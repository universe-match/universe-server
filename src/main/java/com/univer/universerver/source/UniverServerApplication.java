package com.univer.universerver.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UniverServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniverServerApplication.class, args);
    }

}
