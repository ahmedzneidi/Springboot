package tn.esprit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // activer la planitfication des taches
@SpringBootApplication
public class AtelierSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtelierSpringBootApplication.class, args);

    }

}
