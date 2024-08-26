module com.example.pingpong {
    requires javafx.controls;
    requires javafx.fxml;
    

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    // Export and open the subpackages if they contain classes that are used by FXML or reflection
    opens com.example.pingpong.Controller to javafx.fxml;
    exports com.example.pingpong.Controller;

    opens com.example.pingpong.Model to javafx.fxml;
    exports com.example.pingpong.Model;

    opens com.example.pingpong.View to javafx.fxml;
    exports com.example.pingpong.View;
    exports com.example.pingpong;
    opens com.example.pingpong to javafx.fxml;
}