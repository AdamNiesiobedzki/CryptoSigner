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
        stage.setScene(new Scene(fxmlLoader.load(), 1080, 900));
        stage.show();
        MainController controller = fxmlLoader.getController();
        controller.detectUsbDevice();
    }

    public static void main(String[] args) {
        launch();
    }
}