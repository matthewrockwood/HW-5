package com.example.logindemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML private Label welcomeText;

    @FXML private TextField loginEmailField; // changed from usernameField
    @FXML private PasswordField passwordField;

    @FXML private TextField regUsernameField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;

    public void initialize(){
        DBManager.createUserTable();
    }

    @FXML
    protected void handleLoginButton() {
        String email = loginEmailField.getText(); // fixed
        String pass = passwordField.getText();

        boolean success = PasswordManager.authenticate(email, pass);


        System.out.println(success ? "Login successful!" : "Login failed!");
    }

    @FXML
    protected void goToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    protected void handleRegisterButton() {
        String email = regEmailField.getText();
        String pass = regPasswordField.getText();
        byte[] saltBytes = PasswordManager.generateSalt();
        String salt = PasswordManager.byteArrayToString(saltBytes);
        String hash = PasswordManager.generatePasswordHash(pass, saltBytes);

        User newUser = new User(email, hash, salt);

        DBManager.addUser(newUser);

        System.out.println("🧂 Salt (New): " + salt);
        System.out.println("🔑 Hash (New): " + hash);


        System.out.println("User registered: " + email);
    }

    @FXML
    protected void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
