package bsk.adamniesiobedzki.cryptosigner;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RSAKeyGenerator {

    public static void printKeys() {
        KeyPair keyPair = generateRsaKeys();
        if (keyPair != null) {
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            System.out.println("Klucz publiczny: " + java.util.Base64.getEncoder().encodeToString(publicKeyBytes));
            System.out.println("Klucz prywatny: " + java.util.Base64.getEncoder().encodeToString(privateKeyBytes));
        }
    }

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
}
