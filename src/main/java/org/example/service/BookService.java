package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.models.Book;

import java.io.IOException;
import java.util.ArrayList;

import static org.example.service.UtilService.*;

public class BookService {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static ArrayList<Book> getAllBooks(String jwt) throws IOException, ParseException {

        //Skapa ett objekt av HttpGet klassen. INkludera URL
        HttpGet request = new HttpGet("http://localhost:8080/books");

        //Inludera en Authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        //Exekvera Request
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error uppstod");
            return null;
        }

        //Visa upp response payload i console
        HttpEntity entity = response.getEntity();
        //System.out.println(EntityUtils.toString(entity));

        //Konvertera ResponsePayload till användbart objekt.
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Book> books = mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<Book>>() {});

        //Gå igenom och skriv ut books
        for (Book book : books) {
            System.out.println(String.format("Boken %s är skriven av %s", book.getTitle(), book.getAuthor()));
        }

        return books;
    }

    public static void getOneBook(int id, String jwt) throws IOException, ParseException {
        //Skapa ett objekt av HttpGet klassen. INkludera URL
        HttpGet request = new HttpGet(String.format("http://localhost:8080/books/%d", id));

        //Inludera en Authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        //Exekvera Request
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println("Error uppstod");
            return;
        }

        //Visa upp response payload i console
        HttpEntity entity = response.getEntity();
        //System.out.println(EntityUtils.toString(entity));

        //Konvertera ResponsePayload till användbart objekt.
        ObjectMapper mapper = new ObjectMapper();
        Book book = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Book>() {});

        //Gå igenom och skriv ut books
        System.out.println(String.format("Boken %s är skriven av %s. Den har ID %d", book.getTitle(), book.getAuthor(), book.getId()));
    }
    private static Book createBook() {
        Book newBook = new Book();

        newBook.setTitle(getStringInput("Skriv in titeln på boken: "));
        newBook.setAuthor(getStringInput("Skriv in författaren för boken: "));

        return newBook;
    }

    public static void addBook(String jwt) throws IOException, ParseException {
        //Skapa ett objekt av book klassen
        //Book newBook = new Book(4, "Dracula", "Bram Stoker");
        Book newBook = createBook();

        //Skapa ett HttpPost request object
        HttpPost request = new HttpPost("http://localhost:8080/books");

        //Skapa och inkludera en Payload till request
        ObjectMapper mapper = new ObjectMapper();
        StringEntity payload = new StringEntity(mapper.writeValueAsString(newBook), ContentType.APPLICATION_JSON);

        //Inkludera payload till request
        request.setEntity(payload);

        //Inludera en Authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        CloseableHttpResponse response = httpClient.execute(request);

        //Hantera response
        if (response.getCode() != 200) {
            System.out.println("Fel uppstod");
            return;
        }

        HttpEntity entity = response.getEntity();

        Book responseBook = mapper.readValue(EntityUtils.toString(entity), new TypeReference<Book>() {});

        //Kontrollera att returnObejkt har samma värden.
        if (responseBook.getTitle().equals(newBook.getTitle()) &&
                responseBook.getAuthor().equals(newBook.getAuthor())) {
            System.out.println("Success för new book!");
        } else {
            System.out.println("Something went wrong!");
        }
    }

    public static void getOneBookByTitle(String jwt) throws IOException {

        //Get BookTitle from user
        String title = getStringInput("Skriv in namnet på den bok du söker:");

        //Skapa en Request
        HttpGet request = new HttpGet(String.format("http://localhost:8080/books/book/%s", title));

        //Inludera en Authorization metod till request
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        //Skicka request
        CloseableHttpResponse response = httpClient.execute(request);

        //Get Payload
        HttpEntity payload = response.getEntity();

        //Konvertera Payload till Book
        ObjectMapper mapper = new ObjectMapper();
        try {
            Book book = mapper.readValue(EntityUtils.toString(payload), new TypeReference<Book>() {});
            System.out.println(book.toString());
        }  catch (Exception e) {
            System.out.println("Ett fel har skett!");
            System.out.println(e.getMessage());
        }

    }
}
