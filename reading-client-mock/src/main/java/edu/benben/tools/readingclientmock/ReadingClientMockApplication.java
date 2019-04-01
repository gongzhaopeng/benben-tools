package edu.benben.tools.readingclientmock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadingClientMockApplication {

    public static void main(String[] args) {

        var applicationContext =
                SpringApplication.run(ReadingClientMockApplication.class, args);

        applicationContext.getBean(ReadingClientMockMachine.class).launch();
    }

}
