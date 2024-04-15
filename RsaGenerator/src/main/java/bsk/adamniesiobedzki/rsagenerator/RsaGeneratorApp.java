package bsk.adamniesiobedzki.rsagenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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
}