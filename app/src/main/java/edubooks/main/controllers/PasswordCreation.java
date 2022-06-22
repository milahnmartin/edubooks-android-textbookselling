package edubooks.main.controllers;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCreation {

    Context mContext;
    public PasswordCreation(Context context) {
        mContext=context;
    }

    public static String encryptPassword(String PasswordStr) {
        String EncryptedPasswordStr = null;
        try
        {
            /* MessageDigest instance for MD5. */
            MessageDigest m = MessageDigest.getInstance("MD5");

            /* Add plain-text password bytes to digest using MD5 update() method. */
            m.update(PasswordStr.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = m.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            EncryptedPasswordStr = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return EncryptedPasswordStr;
    }
}