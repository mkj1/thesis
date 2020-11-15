package com.example.tokenservice;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public String token() {
        return manager.getToken();
    }

    @GetMapping("/check")
    public boolean check(@RequestHeader("Authorization") String token) {
        return manager.checkToken(token);
    }
}
