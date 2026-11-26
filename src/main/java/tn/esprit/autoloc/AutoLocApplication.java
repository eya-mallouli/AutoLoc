package tn.esprit.autoloc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutoLocApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoLocApplication.class, args);
    }

}
