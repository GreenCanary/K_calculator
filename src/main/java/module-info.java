module com.mike.kcl {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens com.mike.kcl to javafx.fxml;
    exports com.mike.kcl;
}