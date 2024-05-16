package bsk.adamniesiobedzki.cryptosigner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Helpers {
    private static final byte[] SALT = {
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44,
            (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88,
            (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC,
            (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, (byte) 0x00
    };
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    public static SecretKey getKeyFromPin(String pin)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(pin.toCharArray(), SALT, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
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
    public static String getLastModifiedDate(File file) {
        long lastModifiedTime = file.lastModified();
        Date lastModifiedDate = new Date(lastModifiedTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return dateFormat.format(lastModifiedDate);
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot + 1);
        } else {
            return "";
        }
    }

    public static byte[] calculateFileHash(File file) throws NoSuchAlgorithmException, IOException {
        byte[] data = Files.readAllBytes(file.toPath());
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    public static Element getElementByName(Document doc, String elementName) {
        NodeList nodeList = doc.getElementsByTagName(elementName);
        return (Element) nodeList.item(0);
    }
}
