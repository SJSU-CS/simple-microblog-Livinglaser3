package edu.sjsu.cmpe272.simpleblog.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class GenerateKeyPair {

    private static final Logger logger = LoggerFactory.getLogger(GenerateKeyPair.class);

    public static Map<String, String> generateRSAKeyPair() {
        try {
            Map<String, String> keys = new HashMap<>();

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            keys.put("publicKey", publicKeyString);
            keys.put("privateKey", privateKeyString);

            logger.info("RSA key pair generated successfully");
            return keys;

        } catch (NoSuchAlgorithmException e) {
            logger.error("Error generating RSA key pair", e);
            return null;
        }
    }

    public static PrivateKey convertStringToPrivateKey(String keyStr, String algorithm) throws Exception {
        byte[] encoded = Base64.getDecoder().decode(keyStr);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }

    public static String encodePublicKeyToPEM(String base64Encoded) {
        StringBuilder pemFormat = new StringBuilder();
        pemFormat.append("-----BEGIN PUBLIC KEY-----\n");

        int index = 0;
        while (index < base64Encoded.length()) {
            int endIndex = Math.min(index + 64, base64Encoded.length());
            pemFormat.append(base64Encoded.substring(index, endIndex));
            pemFormat.append("\n");
            index = endIndex;
        }

        pemFormat.append("-----END PUBLIC KEY-----");
        return pemFormat.toString();
    }
}
