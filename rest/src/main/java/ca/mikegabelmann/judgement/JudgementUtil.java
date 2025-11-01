package ca.mikegabelmann.judgement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;


public class JudgementUtil {
    private JudgementUtil() {}


    /**
     * Get a random 'salt' for encryption purposes.
     * @return
     */
    public static byte[] getRandomSalt(final int saltLength) {
        byte[] salt = new byte[saltLength];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static String base64Encode(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     *
     * @param s
     * @return
     */
    public static String base64Encode(final String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    /**
     * URL encode a string.
     * @param value
     * @return
     */
    public static String urlEncode(final String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param hash
     * @return
     */
    public static String bytesToHex(final byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

}
