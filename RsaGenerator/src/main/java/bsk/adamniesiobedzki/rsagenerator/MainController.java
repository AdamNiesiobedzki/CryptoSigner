package bsk.adamniesiobedzki.rsagenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.security.KeyPair;

public class MainController {
    @FXML
    public TextField pinField;
    @FXML
    public void generateKey(ActionEvent actionEvent) {
        KeyPair KeyPair = RsaGenerator.generateRsaKeys();
    }
}
