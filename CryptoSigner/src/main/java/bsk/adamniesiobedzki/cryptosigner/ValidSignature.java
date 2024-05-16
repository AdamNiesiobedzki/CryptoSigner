package bsk.adamniesiobedzki.cryptosigner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import static bsk.adamniesiobedzki.cryptosigner.Helpers.getElementByName;

public class ValidSignature {

    private String localization;
    private String fileSize;

    private String user;

    private String fileExtension;

    private String signDate;

    private String lastModificationDate;

    public ValidSignature(Document signature){
        localization = getElementByName(signature, "SignatureProductionPlace").getTextContent();
        fileSize = getElementByName(signature, "FileSize").getTextContent();
        user = getElementByName(signature, "SigningCertificate").getTextContent();
        fileExtension = getElementByName(signature, "FileExtension").getTextContent();
        signDate = getElementByName(signature, "SigningTime").getTextContent();
        lastModificationDate = getElementByName(signature, "LastModified").getTextContent();
    }

    public void open() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Signature Data");
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        root.getChildren().add(vbox);
        Label label = new Label("Signature is valid");
        label.setStyle("-fx-font-weight: bold;");
        vbox.getChildren().add(label);

        Label signatureDataLabel = new Label("Signature was created ");
        signatureDataLabel.setStyle("-fx-font-weight: bold;");
        Label userLabel = new Label("By: " + user);
        Label localizationLabel = new Label("In: " + localization);
        Label signDateLabel = new Label("At: " + signDate);
        Label signedFileDataLabel = new Label("Signed file" );
        signedFileDataLabel.setStyle("-fx-font-weight: bold;");
        Label fileSizeLabel =  new Label("Size: " + fileSize + " bytes");
        Label extensionLabel = new Label("Extension: " + fileExtension);
        Label modificationDate = new Label("Modification date " + lastModificationDate);

        vbox.getChildren().add(signatureDataLabel);
        vbox.getChildren().add(userLabel);
        vbox.getChildren().add(localizationLabel);
        vbox.getChildren().add(signDateLabel);
        vbox.getChildren().add(signedFileDataLabel);
        vbox.getChildren().add(fileSizeLabel);
        vbox.getChildren().add(extensionLabel);
        vbox.getChildren().add(modificationDate);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        vbox.getChildren().add(closeButton);

        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
}
