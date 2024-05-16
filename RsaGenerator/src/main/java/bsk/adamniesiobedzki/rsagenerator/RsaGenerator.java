package bsk.adamniesiobedzki.rsagenerator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class RsaGenerator {
    private static final Random random = new SecureRandom();
    static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    private static final byte[] SALT = {
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44,
            (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88,
            (byte) 0x99, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC,
            (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, (byte) 0x00
    };

    public static KeyPair generateRsaKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom());
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveRsaKeys(Path path, String pin) {
        KeyPair keyPair = generateRsaKeys();
        if (keyPair != null) {
            try (FileOutputStream publicKeyFile = new FileOutputStream(path.resolve("public_key.pem").toString());
                 FileOutputStream privateKeyFile = new FileOutputStream(path.resolve("private_key.pem").toString())) {
                byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
                byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
                SecretKey pinKey = getKeyFromPin(pin);
                IvParameterSpec iv =  generateIv();
                byte[] privateKeyEncryptedBytes = encrypt(privateKeyBytes, pinKey, iv);
                publicKeyFile.write(publicKeyBytes);
                privateKeyFile.write(privateKeyEncryptedBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static SecretKey getKeyFromPin(String pin)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(pin.toCharArray(), SALT, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] encrypt(byte[] input, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedInput = cipher.doFinal(input);
        try {
            outputStream.write(encryptedInput);
            outputStream.write(iv.getIV());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public static byte[] decrypt(byte[] input, SecretKey key)
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

