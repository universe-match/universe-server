package com.univer.universerver.source;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UniverServerApplication extends SpringBootServletInitializer{

	
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";
    public static void main(String[] args) {
        new SpringApplicationBuilder(UniverServerApplication.class)
        .properties(APPLICATION_LOCATIONS)
        .run(args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	return super.configure(builder);
    }

}
