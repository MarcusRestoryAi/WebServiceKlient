package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Hello world!");
/*
        addBook();
        getAllBooks();

        getOneBook(1);
        getOneBook(2);
        getOneBook(3);

 */

        getOneBookByTitle();


    }

    public static void getAllBooks() throws IOException, ParseException {

        //Skapa ett objekt av HttpGet klassen. INkludera URL
        HttpGet request = new HttpGet("http://localhost:8080/books");

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
        ArrayList<Book> books = mapper.readValue(EntityUtils.toString(entity), new TypeReference<ArrayList<Book>>() {});

        //Gå igenom och skriv ut books
        for (Book book : books) {
            System.out.println(String.format("Boken %s är skriven av %s", book.getTitle(), book.getAuthor()));
        }
    }

    public static void getOneBook(int id) throws IOException, ParseException {
        //Skapa ett objekt av HttpGet klassen. INkludera URL
        HttpGet request = new HttpGet(String.format("http://localhost:8080/books/%d", id));

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

    private static String getStringInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt);
        String input = scan.nextLine();

        //Kontroll
        if (input.equals("")) {
            System.out.println("Felaktig input. Försök igen.");
            return getStringInput(prompt);
        }
        return input;
    }

    public static void addBook() throws IOException, ParseException {
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

    public static void getOneBookByTitle() throws IOException {

        //Get BookTitle from user
        String title = getStringInput("Skriv in namnet på den bok du söker:");

        //Skapa en Request
        HttpGet request = new HttpGet(String.format("http://localhost:8080/books/book/%s", title));

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