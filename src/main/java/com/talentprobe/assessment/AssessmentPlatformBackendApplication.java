package com.talentprobe.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssessmentPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentPlatformBackendApplication.class, args);
    }

}
