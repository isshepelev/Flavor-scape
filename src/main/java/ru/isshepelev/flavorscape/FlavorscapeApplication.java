package ru.isshepelev.flavorscape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FlavorscapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlavorscapeApplication.class, args);
    }

}
