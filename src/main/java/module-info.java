module com.example.reflection {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;


    opens com.example.reflection to javafx.fxml;
    exports com.example.reflection;
}