package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.models.Book;
import org.example.models.LoginResponse;
import org.example.models.User;

import java.io.IOException;

import static org.example.service.UtilService.createPayload;
import static org.example.service.UtilService.getStringInput;

public class UserService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void register() throws IOException, ParseException {

        //Skapa ett username och password
        String username = getStringInput("Ange ett nytt användarnamn:");
        String password = getStringInput("Ange ett nytt lösenord:");

        //Skapa ett nytt User objekt
        User newUser = new User(0L, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/auth/register");

        //Skapa en Payload till Request
        request.setEntity(createPayload(newUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid registrering");
            return;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        User responseUser = mapper.readValue(EntityUtils.toString(payload), new TypeReference<User>() {});

        //output till User
        System.out.println(String.format("Användaren %s har skapats med ID %d", responseUser.getUsername(), responseUser.getId()));
    }

    public static LoginResponse login() throws IOException, ParseException {
        //Ange username och password
        String username = getStringInput("Ange användarnamn:");
        String password = getStringInput("Ange lösenord:");

        //Skapa ett nytt User objekt
        User loginUser = new User(0L, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/auth/login");

        //Skapa en Payload till Request
        request.setEntity(createPayload(loginUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid Inloggning");
            return null;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});

        if (loginResponse.getUser() == null) {
            System.out.println("Felaktigt användarnamn eller lösenord");
            return null;
        }

        System.out.println(String.format("Inloggad som %s", loginResponse.getUser().getUsername()));
        System.out.println(String.format("JWT token: %s", loginResponse.getJwt()));
        return loginResponse;
    }

    public static LoginResponse login(String username, String password) throws IOException, ParseException {

        //Skapa ett nytt User objekt
        User loginUser = new User(0L, username, password);

        //Skapa ett nytt request
        HttpPost request = new HttpPost("http://localhost:8080/auth/login");

        //Skapa en Payload till Request
        request.setEntity(createPayload(loginUser));

        //Send request
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200 ) {
            System.out.println("Något har gått fel vid Inloggning");
            return null;
        }

        //Hämta Payload från repsonse
        HttpEntity payload = response.getEntity();

        //Skapa User objekt från payload
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(EntityUtils.toString(payload), new TypeReference<LoginResponse>() {});

        if (loginResponse.getUser() == null) {
            System.out.println("Felaktigt användarnamn eller lösenord");
            return null;
        }

        System.out.println(String.format("Inloggad som %s", loginResponse.getUser().getUsername()));
        System.out.println(String.format("JWT token: %s", loginResponse.getJwt()));
        return loginResponse;
    }
}
