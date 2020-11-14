package com.example.shipping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
public class ShippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingApplication.class, args);

    }

    @GetMapping("/nosecurity")
    public String nosecurity(@RequestParam(value = "name", defaultValue = "from nosecurity - user service") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "from user service") String name) {
        try {
            URL url = new URL("http://localhost:8080/hello");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty ("Authorization", "Bearer testtoken");
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            if(status == 401)
                return status + " Unauthorized";
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return String.format("Hello %s!", name + content);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ShippingApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShippingApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return String.format("failed to contact inventory service");
    }

}
