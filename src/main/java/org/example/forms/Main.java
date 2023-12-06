package org.example.forms;

import org.apache.hc.core5.http.ParseException;
import org.example.models.LoginResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static org.example.service.UserService.login;

public class Main {
    private JPanel form;
    private JTextField txtName;
    private JButton klickaPåMigFörButton;
    private JLabel lblOutput;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton loggaInSomAnvändareButton;


    private LoginResponse login;

    private static JFrame jFrame;

    public Main() {
        klickaPåMigFörButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Denna metod aktiveras när vår knapp blir klickad

                //Hämta textvärde från txtFält
                String name = txtName.getText();

                //Generera hälsningsmeddleande
                String message = String.format("Hejsan %s", name);

                //Skriv ut meddelande till lblOutput
                lblOutput.setText(message);

                //Skriv ut meddelandet i en pop-up
                JOptionPane.showMessageDialog(null, message);
            }
        });
        loggaInSomAnvändareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Utföra en inloggning
                String username = txtUsername.getText();
                String password = String.valueOf(txtPassword.getPassword());

                try {
                    login = login(username, password);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                if (login == null) {
                    lblOutput.setText("Inloggning misslyckades");
                } else {
                    lblOutput.setText(String.format("Inloggning lyckades, som %s", login.getUser().getUsername()));

                    //booksForm = new BooksForm();

                    //BooksForm booksForm = new BooksForm();

                    //BooksForm booksForm1 = new BooksForm();

                    jFrame.setContentPane(new BooksForm(login).form);


                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().form);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //frame.setSize(200, 200);
        frame.setVisible(true);

        jFrame = frame;
    }
}
