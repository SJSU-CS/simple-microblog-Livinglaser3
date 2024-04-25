package edu.sjsu.cmpe272.simpleblog.client;

import java.security.*;
import java.util.Base64;
import static edu.sjsu.cmpe272.simpleblog.client.GenerateKeyPair.convertStringToPrivateKey;


public class SignatureSetter {

    public static String createSignature(PostMessage postMessage, String privateKey)
            throws Exception {


        String data = postMessage.date+postMessage.author+postMessage.message+postMessage.attachment;

        Signature signature = Signature.getInstance("SHA256withRSA");
        PrivateKey pk = convertStringToPrivateKey(privateKey,"RSA");
        signature.initSign(pk);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();


        String encodedSignature = Base64.getEncoder().encodeToString(digitalSignature);

        System.out.println("Signature: " + encodedSignature);
        return encodedSignature;
    }
}

