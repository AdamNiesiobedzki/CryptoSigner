package bsk.adamniesiobedzki.cryptosigner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class MainController {
    @FXML
    public Label fileToEncryptPathLabel;
    @FXML
    public Label encryptorOutputPathLabel;
    @FXML
    public Label encryptorPublicKeyPathLabel;
    @FXML
    public Label encryptorErrorLabel;
    @FXML
    public TextField decryptorPinField;
    @FXML
    public Label decryptorPrivateKeyPathLabel;
    @FXML
    public Label decryptorOutputPathLabel;
    @FXML
    public Label fileToDecryptPathLabel;
    @FXML
    public Label decryptorErrorLabel;
    @FXML
    public Label fileToSignLabel;
    @FXML
    public Label signerPrivateKeyPathLabel;
    @FXML
    public Label signerErrorLabel;
    @FXML
    public TextField formSignPin;
    @FXML
    public TextField formSignLocation;
    @FXML
    public TextField formSignUsername;
    public Label signerPublicKeyPathLabel;
    public Label signerFileToVerifyPathLabel;
    public Label signerSignaturePathLabel;
    public Label verifySignatureErrorLabel;

    private Path encryptorOutputPath = Paths.get("");
    private Path decryptorOutputPath = Paths.get("");
    private File decryptorPrivateKey = null;
    private File encryptorPublicKey = null;
    private File fileToEncode = null;
    private File fileToDecode = null;
    private File signerPrivateKey = null;
    private File signerPublicKey = null;
    private File fileToSign = null;
    private File fileToVerify = null;
    private File signatureToVerify = null;


    public void selectEncryptorPublicKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select public key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        encryptorPublicKey = fileChooser.showOpenDialog(encryptorPublicKeyPathLabel.getScene().getWindow());
        encryptorPublicKeyPathLabel.setText("Selected public key: " + encryptorPublicKey.getAbsolutePath());
    }

    public void selectFileToEncode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to encode");
        fileToEncode = fileChooser.showOpenDialog(fileToEncryptPathLabel.getScene().getWindow());
        fileToEncryptPathLabel.setText("Selected file to encode: " + fileToEncode.getAbsolutePath());
    }

    public void selectEncryptorOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        encryptorOutputPath = directoryChooser.showDialog(encryptorOutputPathLabel.getScene().getWindow()).toPath();
        encryptorOutputPathLabel.setText("Selected output folder: " + encryptorOutputPath);
    }

    public void selectFileToDecode() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to decode");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.enc)", "*.enc");
        fileChooser.getExtensionFilters().add(extFilter);
        fileToDecode = fileChooser.showOpenDialog(fileToDecryptPathLabel.getScene().getWindow());
        fileToDecryptPathLabel.setText("Selected file to decode: " + fileToDecode.getAbsolutePath());
    }

    public void selectPrivateKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select private key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        decryptorPrivateKey = fileChooser.showOpenDialog(decryptorPrivateKeyPathLabel.getScene().getWindow());
        decryptorPrivateKeyPathLabel.setText("Selected private key: " + decryptorPrivateKey.getAbsolutePath());

    }

    public void selectDecryptorOutputFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Output Folder");
        decryptorOutputPath = directoryChooser.showDialog(decryptorOutputPathLabel.getScene().getWindow()).toPath();
        decryptorOutputPathLabel.setText("Selected output folder: " + decryptorOutputPath);
    }

    public void selectSignPrivateKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select private key file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pem)", "*.pem");
        fileChooser.getExtensionFilters().add(extFilter);
        signerPrivateKey = fileChooser.showOpenDialog(signerPrivateKeyPathLabel.getScene().getWindow());
        signerPrivateKeyPathLabel.setText("Selected private key: " + signerPrivateKey.getAbsolutePath());
    }

    public void selectFileToSign() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to sign");
        fileToSign = fileChooser.showOpenDialog(fileToSignLabel.getScene().getWindow());
        fileToSignLabel.setText("Selected file to sign: " + fileToSign.getAbsolutePath());
    }

    public void selectSignPublicKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select public key file");
        signerPublicKey = fileChooser.showOpenDialog(signerPublicKeyPathLabel.getScene().getWindow());
        signerPublicKeyPathLabel.setText("Selected public key: " + signerPublicKey.getAbsolutePath());
    }

    public void selectSignatureToVerify() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select signature to verify");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.xades)", "*.xades");
        fileChooser.getExtensionFilters().add(extFilter);
        signatureToVerify = fileChooser.showOpenDialog(signerSignaturePathLabel.getScene().getWindow());
        signerSignaturePathLabel.setText("Selected signature: " + signatureToVerify.getAbsolutePath());
    }

    public void selectFileToVerify() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to verify");
        fileToVerify = fileChooser.showOpenDialog(signerFileToVerifyPathLabel.getScene().getWindow());
        signerFileToVerifyPathLabel.setText("Selected file to verify: " + fileToVerify.getAbsolutePath());
    }

    public void decryptFile() throws Exception {
        if(!decryptFormValidation()){
            return;
        }
        FileDecryptor.decryptFile(fileToDecode, decryptorOutputPath, decryptorPrivateKey, decryptorPinField.getText());
        decryptorErrorLabel.setText("File decrypted");
    }

    public void encryptFile() throws Exception {
        if(!encryptFormValidation()){
            return;
        }
        FileEncryptor.encryptFile(fileToEncode, encryptorOutputPath, encryptorPublicKey);
        encryptorErrorLabel.setText("File encrypted");
    }

    public void signFile() throws Exception {
        if(!signFormValidation()){
            return;
        }
        System.out.println(formSignLocation.getText() + formSignUsername.getText() + formSignPin.getText());
        FileSigner.signFile(
                fileToSign,
                signerPrivateKey,
                formSignPin.getText(),
                formSignUsername.getText(),
                formSignLocation.getText()
        );
    }

    public void verifySignature() {
        if(!verifySignatureFormValidation()){
            return;
        }
        verifySignatureErrorLabel.setText(
                SignatureVerifier.verifySignature(fileToVerify, signerPublicKey, signatureToVerify));
    }

    private boolean encryptFormValidation(){
        encryptorErrorLabel.setText("");
        Path emptyPath = Paths.get("");
        if (fileToEncode == null) {
            encryptorErrorLabel.setText("Select file to encrypt!");
            return false;
        }
        if (!Files.exists(fileToEncode.toPath())) {
            encryptorErrorLabel.setText("Selected file to encrypt does not exist!");
            return false;
        }
        if (encryptorPublicKey == null) {
            encryptorErrorLabel.setText("Select public key!");
            return false;
        }
        if (!Files.exists(encryptorPublicKey.toPath())) {
            encryptorErrorLabel.setText("Selected public key does not exist!");
            return false;
        }
        if (encryptorOutputPath.equals(emptyPath)) {
            encryptorErrorLabel.setText("Select output path!");
            return false;
        }
        if (!Files.exists(encryptorOutputPath)) {
            encryptorErrorLabel.setText("Selected output path does not exist!");
            return false;
        }
        return true;
    }

    private boolean decryptFormValidation(){
        decryptorErrorLabel.setText("");
        Path emptyPath = Paths.get("");
        String pin = decryptorPinField.getText();
        if (pin.isEmpty()) {
            decryptorErrorLabel.setText("Enter PIN!");
            return false;
        }
        if (fileToDecode == null) {
            decryptorErrorLabel.setText("Select file to decrypt!");
            return false;
        }
        if (!Files.exists(fileToDecode.toPath())) {
            decryptorErrorLabel.setText("Selected file to decrypt does not exist!");
            return false;
        }
        if (decryptorPrivateKey == null) {
            decryptorErrorLabel.setText("Select private key!");
            return false;
        }
        if (!Files.exists(decryptorPrivateKey.toPath())) {
            decryptorErrorLabel.setText("Selected private key does not exist!");
            return false;
        }
        if (decryptorOutputPath.equals(emptyPath)) {
            decryptorErrorLabel.setText("Select output path!");
            return false;
        }
        if (!Files.exists(decryptorOutputPath)) {
            decryptorErrorLabel.setText("Selected output path does not exist!");
            return false;
        }
        return true;
    }

    private boolean signFormValidation(){
        signerErrorLabel.setText("");
        if (formSignPin.getText().isEmpty()) {
            signerErrorLabel.setText("Enter PIN!");
            return false;
        }
        if (formSignUsername.getText().isEmpty()) {
            signerErrorLabel.setText("Enter username!");
            return false;
        }
        if (formSignLocation.getText().isEmpty()) {
            signerErrorLabel.setText("Enter location!");
            return false;
        }
        if (fileToSign == null) {
            signerErrorLabel.setText("Select file to sign!");
            return false;
        }
        if (!Files.exists(fileToSign.toPath())) {
            signerErrorLabel.setText("Selected file to sign does not exist!");
            return false;
        }
        if (signerPrivateKey == null) {
            signerErrorLabel.setText("Select private key!");
            return false;
        }
        if (!Files.exists(signerPrivateKey.toPath())) {
            signerErrorLabel.setText("Selected private key does not exist!");
            return false;
        }
        return true;
    }

    private boolean verifySignatureFormValidation(){
        verifySignatureErrorLabel.setText("");
        if (fileToVerify == null) {
            verifySignatureErrorLabel.setText("Select file to verify!");
            return false;
        }
        if (!Files.exists(fileToVerify.toPath())) {
            verifySignatureErrorLabel.setText("Selected file to verify does not exist!");
            return false;
        }
        if (signerPublicKey == null) {
            verifySignatureErrorLabel.setText("Select public key!");
            return false;
        }
        if (!Files.exists(signerPublicKey.toPath())) {
            verifySignatureErrorLabel.setText("Selected public key does not exist!");
            return false;
        }
        if (signatureToVerify == null) {
            verifySignatureErrorLabel.setText("Select signature to verify!");
            return false;
        }
        if (!Files.exists(signatureToVerify.toPath())) {
            verifySignatureErrorLabel.setText("Selected signature does not exist!");
            return false;
        }
        return true;
    }
}
