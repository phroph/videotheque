/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.utils;

import com.sun.jersey.core.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.Videotheque.ejb.CustomerService;

/**
 *
 * @author Phillip
 */
public class Encryption {
    public static String hashSHAHex(String password) {
        try {
            return bytesToHex(MessageDigest.getInstance("SHA").digest(password.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static String hashSHABase64(String password) {
        try {
            return new String(Base64.encode(MessageDigest.getInstance("SHA").digest(password.getBytes())));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
