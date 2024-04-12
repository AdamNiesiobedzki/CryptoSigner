package bsk.adamniesiobedzki.rsagenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.KeyPair;

import static bsk.adamniesiobedzki.rsagenerator.RsaGenerator.generateRsaKeys;

public class RsaGeneratorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RsaGeneratorApp.class.getResource("main.fxml"));
        stage.setTitle("RsaGenerator");
        stage.setScene(new Scene(fxmlLoader.load(), 400, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void printKeys() {
        KeyPair keyPair = generateRsaKeys();
        if (keyPair != null) {
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            System.out.println("Klucz publiczny: " + java.util.Base64.getEncoder().encodeToString(publicKeyBytes));
            System.out.println("Klucz prywatny: " + java.util.Base64.getEncoder().encodeToString(privateKeyBytes));
        }
    }


}