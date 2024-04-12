package edu.sjsu.cmpe272.simpleblog.server;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


class VerifySignature {
    public static boolean verifySignature(MessageHandler messageHandler, PublicKey publicKey) throws Exception {
        String signature = messageHandler.getSignature();
        byte[] decodedSignature = Base64.getDecoder().decode(signature);
        String data = messageHandler.getDate() + messageHandler.getAuthor() + messageHandler.getMessage() + messageHandler.getAttachment();
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(decodedSignature);
    }
}
@Service
public class UserService {
    public static Map<String, String> userPublicKeys = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createUser(String username, String publicKey) {
        userPublicKeys.put(username.toLowerCase(), publicKey);
    }

    public String getPublicKey(String username) {
        return userPublicKeys.get(username.toLowerCase());
    }


    //  converting string to PublicKey and PrivateKey
    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        try{
            byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        }
        catch (Exception e){
            return null;
        }
    }

    public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(key.getBytes());
        PKCS8EncodedKeySpec PKCS8privateKey = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(PKCS8privateKey);
    }

    public String getPublicKeyByUsername(String username) {
        if(userPublicKeys.containsKey(username)){
            return userPublicKeys.get(username);
        }
        return null;
    }
}
