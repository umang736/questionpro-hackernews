package com.interview.questionpro.hackernews;

import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackernNewsApplication {
	
	private final static Logger logger = LoggerFactory.getLogger(HackernNewsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HackernNewsApplication.class, args);
		logger.info("HackernewsApplication started");
	}
	
	@PostConstruct
    public void afterSetup(){
    	logger.info("Timezone = {}", TimeZone.getDefault());
    	logger.info("Current dateTime  = {}", LocalDateTime.now().toString() );
    }

    @Bean
    public CommandLineRunner init(){
        return args -> {

        };
    }

}
