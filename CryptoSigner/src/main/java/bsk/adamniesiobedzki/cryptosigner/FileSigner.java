package bsk.adamniesiobedzki.cryptosigner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static bsk.adamniesiobedzki.cryptosigner.Helpers.*;

public class FileSigner {

    public static void signFile(
            File inputFile,
            File privateKeyFile,
            String pin,
            String username,
            String location) throws Exception {
        byte[] inputFileHash = calculateFileHash(inputFile);
        byte[] privateKeyEncryptedBytes = Files.readAllBytes(privateKeyFile.toPath());
        byte[] privateKeyBytes = decryptPrivateKey(privateKeyEncryptedBytes, getKeyFromPin(pin));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        Document doc = generateSignatureTemplate(location, username, inputFile);

        Element digestValue = getElementByName(doc, "ds:DigestValue");
        digestValue.appendChild(doc.createTextNode(Base64.getEncoder().encodeToString(inputFileHash)));

        Element signatureValue = getElementByName(doc, "ds:SignatureValue");
        byte[] signature = generateSignature(privateKey, inputFileHash);
        signatureValue.appendChild(doc.createTextNode(Base64.getEncoder().encodeToString(signature)));

        saveXMLToFile(doc, inputFile.toPath() + ".xades");
    }

    private static void saveXMLToFile(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document generateSignatureTemplate(String place, String user, File signedFile) throws ParserConfigurationException, NoSuchAlgorithmException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().newDocument();

        Element signature = doc.createElement("ds:Signature");
        doc.appendChild(signature);

        Element signedInfo = doc.createElement("ds:SignedInfo");
        signature.appendChild(signedInfo);

        Element canonicalizationMethod = doc.createElement("ds:CanonicalizationMethod");
        canonicalizationMethod.setAttribute("Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");
        signedInfo.appendChild(canonicalizationMethod);

        Element signatureMethod = doc.createElement("ds:SignatureMethod");
        signatureMethod.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
        signedInfo.appendChild(signatureMethod);

        Element reference = doc.createElement("ds:Reference");
        signedInfo.appendChild(reference);

        Element digestMethod = doc.createElement("ds:DigestMethod");
        digestMethod.setAttribute("Algorithm", "http://www.w3.org/2001/04/xmldsig-more#sha256");
        reference.appendChild(digestMethod);

        Element digestValue = doc.createElement("ds:DigestValue");
        reference.appendChild(digestValue);

        Element signatureValue = doc.createElement("ds:SignatureValue");
        signature.appendChild(signatureValue);

        Element object = doc.createElement("ds:Object");
        signature.appendChild(object);

        Element qualifyingProperties = doc.createElement("QualifyingProperties");
        object.appendChild(qualifyingProperties);

        Element signedProperties = doc.createElement("SignedProperties");
        qualifyingProperties.appendChild(signedProperties);

        Element signedSignatureProperties = doc.createElement("SignedSignatureProperties");
        signedProperties.appendChild(signedSignatureProperties);

        Element signingTime = doc.createElement("SigningTime");
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
        String formattedDate = now.format(formatter);
        signingTime.appendChild(doc.createTextNode(formattedDate));
        signedSignatureProperties.appendChild(signingTime);

        Element signingCertificate = doc.createElement("SigningCertificate");
        signingCertificate.appendChild(doc.createTextNode(user));
        signedSignatureProperties.appendChild(signingCertificate);

        Element signatureProductionPlace = doc.createElement("SignatureProductionPlace");
        signatureProductionPlace.appendChild(doc.createTextNode(place));
        signedSignatureProperties.appendChild(signatureProductionPlace);

        Element signedDataObjectProperties = doc.createElement("SignedDataObjectProperties");
        signedProperties.appendChild(signedDataObjectProperties);

        Element signedFileSize = doc.createElement("FileSize");
        signedFileSize.appendChild(doc.createTextNode(String.valueOf(signedFile.getTotalSpace())));
        signedDataObjectProperties.appendChild(signedFileSize);

        Element signedFileExtension = doc.createElement("FileExtension");
        signedFileExtension.appendChild(doc.createTextNode(getFileExtension(signedFile)));
        signedDataObjectProperties.appendChild(signedFileExtension);

        Element lastModified = doc.createElement("LastModified");
        lastModified.appendChild(doc.createTextNode(getLastModifiedDate(signedFile)));
        signedDataObjectProperties.appendChild(lastModified);

        return doc;
    }

    public static byte[] generateSignature(PrivateKey privateKey, byte[] digestValue) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(digestValue);
        return signature.sign();
    }

}
