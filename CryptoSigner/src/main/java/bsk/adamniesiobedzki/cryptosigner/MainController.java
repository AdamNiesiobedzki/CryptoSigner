package bsk.adamniesiobedzki.cryptosigner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainController {
    @FXML
    public Label fileToEncodePathLabel;
    @FXML
    public Label encoderOutputPathLabel;
    @FXML
    public Label publicKeyPathLabel;
    @FXML
    public Label encoderErrorLabel;
    @FXML
    public TextField pinField;
    @FXML
    public Label privateKeyPathLabel;
    @FXML
    public Label decoderOutputPathLabel;
    @FXML
    public Label fileToDecodePathLabel;
    @FXML
    public Label decoderErrorLabel;

    private Path encoderOutputPath = Paths.get("");
    private Path decoderOutputPath = Paths.get("");
    private Path privateKeyPath = Paths.get("");
    private Path publicKeyPath = Paths.get("");
    private Path fileToEncodePath = Paths.get("");
    private Path fileToDecodePath = Paths.get("");


    public void selectPublicKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select public key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        publicKeyPath = fileChooser.showOpenDialog(publicKeyPathLabel.getScene().getWindow()).toPath();
        publicKeyPathLabel.setText("Selected public key: " + publicKeyPath);
    }

    public void selectFileToEncode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to encode");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToEncodePath = fileChooser.showOpenDialog(fileToEncodePathLabel.getScene().getWindow()).toPath();
        fileToEncodePathLabel.setText("Selected file to encode: " + fileToEncodePath);
    }

    public void selectEncoderOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        encoderOutputPath = directoryChooser.showDialog(encoderOutputPathLabel.getScene().getWindow()).toPath();
        encoderOutputPathLabel.setText("Selected output folder: " + encoderOutputPath);
    }

    public void selectFileToDecode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to decode");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToDecodePath = fileChooser.showOpenDialog(fileToDecodePathLabel.getScene().getWindow()).toPath();
        fileToDecodePathLabel.setText("Selected file to decode: " + fileToDecodePath);
    }

    public void selectPrivateKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select private key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        privateKeyPath = fileChooser.showOpenDialog(privateKeyPathLabel.getScene().getWindow()).toPath();
        privateKeyPathLabel.setText("Selected private key: " + privateKeyPath);

    }

    public void selectDecoderOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        decoderOutputPath = directoryChooser.showDialog(decoderOutputPathLabel.getScene().getWindow()).toPath();
        decoderOutputPathLabel.setText("Selected output folder: " + decoderOutputPath);
    }

    public void decodeFile() {
        if(!decodeFormValidation()){
            return;
        }
    }

    public void encodeFile() {
        if(encodeFormValidation()){
            return;
        }
    }

    private boolean encodeFormValidation(){
        encoderErrorLabel.setText("");
        Path emptyPath = Paths.get("");
        if (fileToEncodePath.equals(emptyPath)) {
            encoderErrorLabel.setText("Select file to encode!");
            return false;
        }
        if (!Files.exists(fileToEncodePath)) {
            encoderErrorLabel.setText("Selected file to encode does not exist!");
            return false;
        }
        if (publicKeyPath.equals(emptyPath)) {
            encoderErrorLabel.setText("Select public key!");
            return false;
        }
        if (!Files.exists(publicKeyPath)) {
            encoderErrorLabel.setText("Selected public key does not exist!");
            return false;
        }
        if (encoderOutputPath.equals(emptyPath)) {
            encoderErrorLabel.setText("Select output path!");
            return false;
        }
        if (!Files.exists(encoderOutputPath)) {
            encoderErrorLabel.setText("Selected output path does not exist!");
            return false;
        }
        return true;
    }

    private boolean decodeFormValidation(){
        decoderErrorLabel.setText("");
        Path emptyPath = Paths.get("");
        String pin = pinField.getText();
        if (pin.isEmpty()) {
            decoderErrorLabel.setText("Enter PIN!");
            return false;
        }
        if (fileToDecodePath.equals(emptyPath)) {
            decoderErrorLabel.setText("Select file to decode!");
            return false;
        }
        if (!Files.exists(fileToDecodePath)) {
            decoderErrorLabel.setText("Selected file to decode does not exist!");
            return false;
        }
        if (privateKeyPath.equals(emptyPath)) {
            decoderErrorLabel.setText("Select private key!");
            return false;
        }
        if (!Files.exists(privateKeyPath)) {
            decoderErrorLabel.setText("Selected private key does not exist!");
            return false;
        }
        if (decoderOutputPath.equals(emptyPath)) {
            decoderErrorLabel.setText("Select output path!");
            return false;
        }
        if (!Files.exists(decoderOutputPath)) {
            decoderErrorLabel.setText("Selected output path does not exist!");
            return false;
        }
        return true;
    }
}
