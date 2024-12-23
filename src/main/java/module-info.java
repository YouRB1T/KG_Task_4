module com.cgvsu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.cgvsu to javafx.fxml;
    exports com.cgvsu;
    exports com.cgvsu.render_engine;
    opens com.cgvsu.render_engine to javafx.fxml;
}