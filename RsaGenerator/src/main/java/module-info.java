module bsk.adamniesiobedzki.rsagenerator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens bsk.adamniesiobedzki.rsagenerator to javafx.fxml;
    exports bsk.adamniesiobedzki.rsagenerator;
}