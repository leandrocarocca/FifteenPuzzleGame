module com.example.fifteenpuzzlegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fifteenpuzzlegame to javafx.fxml;
    exports com.example.fifteenpuzzlegame;
}