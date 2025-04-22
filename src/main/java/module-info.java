module com.example.logindemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;
    requires com.github.spotbugs.annotations;


    opens com.example.logindemo to javafx.fxml;
    exports com.example.logindemo;
}