package com.example.inventory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Boolean.parseBoolean;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello(@RequestParam(value = "name", defaultValue = "from demo service") String name, @RequestHeader("Authorization") String token) {

        try {
            URL url = new URL("http://localhost:8082/check");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Authorization", token);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            boolean ret = parseBoolean(content.toString());
            if(ret)
                return new ResponseEntity<String>("OK", HttpStatus.OK);
            else
                return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(InventoryApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InventoryApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ResponseEntity<String>("Unable to reach token service", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
