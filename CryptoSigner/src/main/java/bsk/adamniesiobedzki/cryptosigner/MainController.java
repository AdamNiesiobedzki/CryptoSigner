package bsk.adamniesiobedzki.cryptosigner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
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
    private File privateKey = null;
    private File publicKey = null;
    private File fileToEncode = null;
    private File fileToDecode = null;


    public void selectPublicKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select public key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        publicKey = fileChooser.showOpenDialog(publicKeyPathLabel.getScene().getWindow());
        publicKeyPathLabel.setText("Selected public key: " + publicKey.getAbsolutePath());
    }

    public void selectFileToEncode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to encode");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToEncode = fileChooser.showOpenDialog(fileToEncodePathLabel.getScene().getWindow());
        fileToEncodePathLabel.setText("Selected file to encode: " + fileToEncode.getAbsolutePath());
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.enc)", "*.enc");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToDecode = fileChooser.showOpenDialog(fileToDecodePathLabel.getScene().getWindow());
        fileToDecodePathLabel.setText("Selected file to decode: " + fileToDecode.getAbsolutePath());
    }

    public void selectPrivateKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select private key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        privateKey = fileChooser.showOpenDialog(privateKeyPathLabel.getScene().getWindow());
        privateKeyPathLabel.setText("Selected private key: " + privateKey.getAbsolutePath());

    }

    public void selectDecoderOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        decoderOutputPath = directoryChooser.showDialog(decoderOutputPathLabel.getScene().getWindow()).toPath();
        decoderOutputPathLabel.setText("Selected output folder: " + decoderOutputPath);
    }

    public void decodeFile() throws Exception {
        if(!decodeFormValidation()){
            return;
        }
        FileDecryptor.decryptFile(fileToDecode, decoderOutputPath, privateKey, pinField.getText());
    }

    public void encodeFile() throws Exception {
        if(!encodeFormValidation()){
            return;
        }
        FileEncryptor.encryptFile(fileToEncode, encoderOutputPath, publicKey);
    }

    private boolean encodeFormValidation(){
        encoderErrorLabel.setText("");
        Path emptyPath = Paths.get("");
        if (fileToEncode == null) {
            encoderErrorLabel.setText("Select file to encode!");
            return false;
        }
        if (!Files.exists(fileToEncode.toPath())) {
            encoderErrorLabel.setText("Selected file to encode does not exist!");
            return false;
        }
        if (publicKey == null) {
            encoderErrorLabel.setText("Select public key!");
            return false;
        }
        if (!Files.exists(publicKey.toPath())) {
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
        if (fileToDecode == null) {
            decoderErrorLabel.setText("Select file to decode!");
            return false;
        }
        if (!Files.exists(fileToDecode.toPath())) {
            decoderErrorLabel.setText("Selected file to decode does not exist!");
            return false;
        }
        if (privateKey == null) {
            decoderErrorLabel.setText("Select private key!");
            return false;
        }
        if (!Files.exists(privateKey.toPath())) {
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
