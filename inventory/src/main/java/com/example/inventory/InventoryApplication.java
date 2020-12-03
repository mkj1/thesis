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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@SpringBootApplication
@RestController
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @GetMapping("/productinfo")
    public ResponseEntity<?> productinfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        try {
            var parsed = parseJwt(token);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | io.jsonwebtoken.security.SignatureException ex) {
            Logger.getLogger(InventoryApplication.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<String>("Unauthorized " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
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
            if (ret) {
                return new ResponseEntity<String>("\"products\":\"50\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(InventoryApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InventoryApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ResponseEntity<String>("Unable to reach token service", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //example from https://www.viralpatel.net/java-create-validate-jwt-token/
    public static Jws<Claims> parseJwt(String jwtString) throws InvalidKeySpecException, NoSuchAlgorithmException {

        PublicKey publicKey = getPublicKey();

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }

    //example from https://www.viralpatel.net/java-create-validate-jwt-token/
    //hardcoded key for demonstration purpose
    private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String rsaPublicKey = "-----BEGIN PUBLIC KEY-----"
                + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyu3NB7Tr3nzETLNbZHYi"
                + "ZvgNeg3/OZUJZl40LzBzYGOD/8575eJETjfQ3QXaNyvNThu6Uf9B/V73QUxKI4/+"
                + "rwlbjA3niIga4MdDiY4b9K/KFA+HedvtZF1yE2p4smXGydPLOLBe31EgriGTob78"
                + "EE3f7SMFxlNaqn4Pm7KJkOodnMz0ilwLseeL1IkTtiFn/2OrcMpPHMtTxyDn3pQl"
                + "VCeJM5j/grDh+0YdyTMGdDHOBgM53VqSsDVyo1TNtP2yhPRYCIiI85hEHVaUnVM9"
                + "jGwCjNZLJHWh10Mrmh6B3z8BEmLhMAZXeL4fQBjBd42DLvIIJwM1USKFhjK+XghN"
                + "rQIDAQAB"
                + "-----END PUBLIC KEY-----";
        rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(keySpec);
        return publicKey;
    }

}
