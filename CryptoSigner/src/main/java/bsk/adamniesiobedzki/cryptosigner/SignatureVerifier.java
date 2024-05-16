package bsk.adamniesiobedzki.cryptosigner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static bsk.adamniesiobedzki.cryptosigner.Helpers.*;

public class SignatureVerifier {

    public static String verifySignature(File inputFile, File publicKeyFile, File signatureFile) {

        Document signature = loadXMLFromFile(signatureFile);

        if (signature == null) {
            return "There was an error while loading signature file";
        }
        try{
            if(verifyDigestValue(inputFile, signature) &&
                    verifySignatureValue(signature, publicKeyFile) &&
                    verifySignatureFileSize(signature, inputFile) &&
                    verifySignatureExtension(signature, inputFile) &&
                    verifySignatureModificationDate(signature, inputFile)
            ){
                ValidSignature validSignature = new ValidSignature(signature);
                validSignature.open();
                return "Signature is valid";
            }
            else
                return "Signature is not valid ";
        } catch (Exception e){
            e.printStackTrace();
            return "There was an error while verifying signature";
        }
    }
    public static Document loadXMLFromFile(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyDigestValue(File inputFile, Document signature) throws NoSuchAlgorithmException, IOException {
        byte[] inputFileHash = calculateFileHash(inputFile);
        String digestValue = getElementByName(signature, "ds:DigestValue").getTextContent();
        String fileDigestValue = Base64.getEncoder().encodeToString(inputFileHash);
        return digestValue.equals(fileDigestValue);
    }

    public static boolean verifySignatureValue(Document signature, File publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        String signatureValueBase64 = getElementByName(signature, "ds:SignatureValue").getTextContent();
        String digestValueBase64 = getElementByName(signature, "ds:DigestValue").getTextContent();

        byte[] digestValue = Base64.getDecoder().decode(digestValueBase64);
        byte[] signatureValue = Base64.getDecoder().decode(signatureValueBase64);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(digestValue);
        return sig.verify(signatureValue);
    }

    public static boolean verifySignatureFileSize(Document signature, File inputFile){
        String inputFileSize = String.valueOf(inputFile.getTotalSpace());
        String fileSize = getElementByName(signature, "FileSize").getTextContent();
        return fileSize.equals(inputFileSize);
    }

    public static boolean verifySignatureExtension(Document signature, File inputFile){
        String fileExtension = getElementByName(signature, "FileExtension").getTextContent();
        return fileExtension.equals(getFileExtension(inputFile));
    }

    public static boolean verifySignatureModificationDate(Document signature, File inputFile){
        String lastModified = getElementByName(signature, "LastModified").getTextContent();
        return lastModified.equals(getLastModifiedDate(inputFile));
    }
}
