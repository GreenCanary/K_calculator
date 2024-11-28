module com.mike.kcl {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.mike.kcl to javafx.fxml;
    exports com.mike.kcl;
}