package com.openclassrooms.mddapi.toolLibs;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecretKeyGenerator {

    public static void main(String[] args) {
        try {
            // Générer une clé secrète avec l'algorithme HMAC SHA-256
            SecretKey secretKey = generateSecretKey();

            // Convertir la clé secrète en une représentation Base64 pour stockage ou utilisation
            String base64Key = encodeBase64(secretKey);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        return keyGenerator.generateKey();
    }

    private static String encodeBase64(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
