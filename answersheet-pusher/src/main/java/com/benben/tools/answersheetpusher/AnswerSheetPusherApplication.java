package com.benben.tools.answersheetpusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnswerSheetPusherApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnswerSheetPusherApplication.class, args);
    }
}
