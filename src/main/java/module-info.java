module org.example.mysnake {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;


    opens org.example.mysnake to javafx.fxml;
    exports org.example.mysnake;
}