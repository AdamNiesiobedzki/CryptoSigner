module bsk.adamniesiobedzki.cryptosigner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.bouncycastle.provider;
    requires java.xml;
    requires java.xml.crypto;
    requires usbdrivedetector;

    opens bsk.adamniesiobedzki.cryptosigner to javafx.fxml;
    exports bsk.adamniesiobedzki.cryptosigner;
}