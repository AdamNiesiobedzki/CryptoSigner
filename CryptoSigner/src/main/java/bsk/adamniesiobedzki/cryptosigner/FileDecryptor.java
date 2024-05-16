package bsk.adamniesiobedzki.cryptosigner;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import static bsk.adamniesiobedzki.cryptosigner.Helpers.decryptPrivateKey;
import static bsk.adamniesiobedzki.cryptosigner.Helpers.getKeyFromPin;

public class FileDecryptor {
    private static final Random random = new SecureRandom();
    public static void decryptFile(File inputFile, Path outputPath, File privateKeyFile, String pin) throws Exception {
        byte[] privateKeyEncryptedBytes = Files.readAllBytes(privateKeyFile.toPath());
        byte[] privateKeyBytes = decryptPrivateKey(privateKeyEncryptedBytes, getKeyFromPin(pin));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String encryptedFileName = outputPath.resolve("decryptedFile.txt").toString();
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

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
