package myBlockchain.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SHA256Helper {

    public static String generateHash(String data) {
        var hexadecimalString = new StringBuilder();

        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            for (byte currentByte : hash){
                var hexadecimal = Integer.toHexString(0xff & currentByte);

                if (hexadecimal.length() == 1){
                   hexadecimalString.append('0');
                }

                hexadecimalString.append(hexadecimal);
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return hexadecimalString.toString();
    }
}