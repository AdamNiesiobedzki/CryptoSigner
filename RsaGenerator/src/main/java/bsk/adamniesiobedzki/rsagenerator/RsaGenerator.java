package bsk.adamniesiobedzki.rsagenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class RsaGenerator {
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

    public static void saveRsaKeys(Path path, String PIN) {
        KeyPair keyPair = generateRsaKeys();
        if (keyPair != null) {
            try (FileOutputStream publicKeyFile = new FileOutputStream(path.resolve("public_key.pem").toString());
                 FileOutputStream privateKeyFile = new FileOutputStream(path.resolve("private_key.pem").toString())) {
                byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
                publicKeyFile.write(Base64.getEncoder().encode(publicKeyBytes));
                byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
                privateKeyFile.write(Base64.getEncoder().encode(privateKeyBytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
