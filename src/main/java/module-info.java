module mainproject.lab11gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens View to javafx.fxml;
    exports View;
}