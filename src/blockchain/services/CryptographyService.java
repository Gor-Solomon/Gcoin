package blockchain.services;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

public class CryptographyService {
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

    public static KeyPair ellipticCurveCrypto() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
            keyPairGenerator.initialize(params);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sign(PrivateKey privateKey, String input){
        Signature signature;
        var output = new byte[0];

        try {
            //BC = Bouncy Castle
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(input.getBytes(StandardCharsets.UTF_8));
            output = signature.sign();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

        return output;
    }

    // Checks wether the given transaction belongs to the sender based on the signature.
    public static boolean verify(PublicKey publicKey, String data, byte[] targetSignature){
        Signature signature;
        boolean output;

        try {
            //BC = Bouncy Castle
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initVerify(publicKey);
            output = signature.verify(targetSignature);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

        return output;
    }
}