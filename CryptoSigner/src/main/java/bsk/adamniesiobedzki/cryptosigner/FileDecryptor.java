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

public class FileDecryptor {

    private static final byte[] SALT = {
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44,
            (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88,
            (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC,
            (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, (byte) 0x00
    };

    private static final Random random = new SecureRandom();
    private static final String algorithm = "AES/CBC/PKCS5Padding";
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

    public static SecretKey getKeyFromPin(String pin)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(pin.toCharArray(), getNextSalt(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return SALT;
    }

    public static byte[] decryptPrivateKey(byte[] input, SecretKey key)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(input, input.length - 16, input.length));
        byte[] encryptedBytes = Arrays.copyOfRange(input, 0, input.length - 16);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedBytes);
    }
}
