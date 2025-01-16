module com.mike.kcl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;

    opens com.mike.kcl to javafx.fxml;
    exports com.mike.kcl;
}

