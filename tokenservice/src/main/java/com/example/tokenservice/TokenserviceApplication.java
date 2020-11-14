package com.example.tokenservice;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TokenserviceApplication {

    TokenManager manager = new TokenManager();

    public static void main(String[] args) {
        SpringApplication.run(TokenserviceApplication.class, args);
    }

    @GetMapping("/token")
    public String token(@RequestParam(value = "name", defaultValue = "from token service") String name) {
        return manager.getToken();
    }

    @GetMapping("/check")
    public boolean check(@RequestParam(value = "name", defaultValue = "tokentest") String name) {
        return manager.checkToken(name);
    }
}
