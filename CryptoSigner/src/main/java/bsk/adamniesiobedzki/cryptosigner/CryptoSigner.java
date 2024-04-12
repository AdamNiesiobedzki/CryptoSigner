package bsk.adamniesiobedzki.cryptosigner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CryptoSigner extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CryptoSigner.class.getResource("main.fxml"));
        stage.setTitle("CryptoSigner");
        stage.setScene(new Scene(fxmlLoader.load(), 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}