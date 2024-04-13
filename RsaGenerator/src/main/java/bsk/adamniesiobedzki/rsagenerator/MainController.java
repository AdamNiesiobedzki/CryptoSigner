package bsk.adamniesiobedzki.rsagenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;

public class MainController {
    @FXML
    public TextField pinField;
    public Label errorLabel;

    @FXML
    private Label selectedFolderLabel;

    private Path selectedDirectory = Paths.get("");

    @FXML
    private void generateKey() {
        if(!formValidation()){
            return;
        }
        RsaGenerator.saveRsaKeys(selectedDirectory, pinField.getText());
        errorLabel.setText("RSA keys were saved in chosen directory");
    }
    @FXML
    private void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        selectedDirectory = directoryChooser.showDialog(selectedFolderLabel.getScene().getWindow()).toPath();
        selectedFolderLabel.setText("Selected folder: " + selectedDirectory);
    }

    private boolean formValidation(){
        errorLabel.setText("");
        String pin = pinField.getText();
        if (pin.isEmpty()) {
            errorLabel.setText("Enter PIN!");
            return false;
        }
        if (selectedDirectory.equals(Paths.get(""))) {
            errorLabel.setText("Select directory!");
            return false;
        }
        if (!Files.exists(selectedDirectory)) {
            errorLabel.setText("Selected folder does not exist!");
            return false;
        }
        return true;
    }
}
