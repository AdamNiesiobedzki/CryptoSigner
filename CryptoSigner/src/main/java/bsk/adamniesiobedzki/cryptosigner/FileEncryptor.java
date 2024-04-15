package bsk.adamniesiobedzki.cryptosigner;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class FileEncryptor {

    public static void encryptFile(File inputFile, Path outputPath, File publicKeyFile) throws Exception {
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String encryptedFileName = outputPath.resolve("encryptedFile.enc").toString();

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(encryptedFileName)){
            byte[] inputFileBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputFileBytes);
            byte[] encryptedBytes = cipher.doFinal(inputFileBytes);
            outputStream.write(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
