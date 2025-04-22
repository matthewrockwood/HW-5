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
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private TextField regUsernameField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regPasswordField;

    public void initialize(){
        DBManager.createUserTable();
    }

    @FXML
    protected void handleLoginButton() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        System.out.println("Login attempt: " + user + " / " + pass);
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
        String user = regUsernameField.getText();
        String email = regEmailField.getText();
        String pass = regPasswordField.getText();
        String salt = "";
        System.out.println("Registering: " + user + " / " + email + " / " + pass);


        User newUser = new User(email,salt,pass);
        DBManager.addUser(newUser);
    }

    @FXML
    protected void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}