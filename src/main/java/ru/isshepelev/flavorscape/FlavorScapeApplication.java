package ru.isshepelev.flavorscape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlavorScapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlavorScapeApplication.class, args);
    }

}
