package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class WebhookService {
    private final String clientSecret;

    @Inject
    public WebhookService(Config config) {
        this.clientSecret = config.getString("auth.clientSecret");
    }

    public boolean validateSignature(String signature, JsonNode body) {
        try {
            final String sourceString = clientSecret + body.toString();
            final String expectedHash = generateSha256Hash(sourceString);

            return expectedHash.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String generateSha256Hash(String input) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashBytes = digest.digest(input.getBytes());
        final StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            final String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}
