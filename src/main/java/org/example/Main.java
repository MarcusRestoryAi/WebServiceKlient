package org.example;

import org.apache.hc.core5.http.ParseException;
import org.example.models.LoginResponse;

import java.io.IOException;

import static org.example.service.BookService.*;
import static org.example.service.UserService.*;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Hello world!");

        //register();
        LoginResponse login = login();

        if (login == null) {
            System.out.println("Inloggning misslyckades");
            return;
        }

        getAllBooks(login.getJwt());
        getOneBookByTitle(login.getJwt());
/*
        addBook(login.getJwt());

        getOneBook(1, login.getJwt());

        getOneBookByTitle(login.getJwt());
*/
    }


}