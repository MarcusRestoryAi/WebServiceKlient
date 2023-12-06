package org.example.forms;

import org.apache.hc.core5.http.ParseException;
import org.example.models.Book;
import org.example.models.LoginResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.service.BookService.getAllBooks;

public class BooksForm {
    public JPanel form;
    private JButton hämtaBöckerButton;
    private JTable tblBooks;
    private JTextArea txtBooksOutput;

    private LoginResponse login;

    public BooksForm(LoginResponse login) {

        this.login = login;

        hämtaBöckerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Skicka Request till DB via Service
                ArrayList<Book> books;
                try {
                    books = getAllBooks(login.getJwt());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                //Skapa en ForEach loop där vi skriver ut info om varje bok
                for (Book book : books) {
                    txtBooksOutput.setText( String.format("%s%s", txtBooksOutput.getText() + "\n", book.toString()));
                }


                /*
                DefaultTableModel model = new DefaultTableModel();

                tblBooks = new JTable(model);

                for(Book book : books) {
                    String[] strAtt = new String[] {"a", "b", "c"};
                    model.addRow(strAtt);
                }

                model.fireTableRowsUpdated(0, model.getRowCount() -1);
                 */


            }
        });
    }


}
