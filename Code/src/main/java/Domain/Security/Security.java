package Domain.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getHash(String password) {
        return new String(messageDigest.digest(password.getBytes()));
    }
}
