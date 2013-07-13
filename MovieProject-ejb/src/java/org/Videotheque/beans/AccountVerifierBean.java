/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.Videotheque.ejb.CustomerService;
import org.Videotheque.utils.Encryption;

/**
 *
 * @author Phillip
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class AccountVerifierBean implements Serializable {
    private HashMap<String, String> verificationKeyMapping;
    private HashMap<String, String> userPassMapping;
    
    public AccountVerifierBean() {
        verificationKeyMapping = new HashMap<String, String>();
        userPassMapping = new HashMap<String, String>();
    }
    
    public boolean verifyKey(String username, String key) {
        if(!verificationKeyMapping.containsKey(username)) {
            return false;
        }
        boolean value = key.equals(verificationKeyMapping.get(username));
        if(value) {
            verificationKeyMapping.remove(username);
        }
        return value;
    }
    
    public String getExpectedKey(String username) {
        if(!verificationKeyMapping.containsKey(username)) {
            return null;
        }
        else {
            return verificationKeyMapping.get(username);
        }
    } 
    
    public String getPassword(String username) {
        if(userPassMapping.containsKey(username)) {
            String pw = userPassMapping.get(username);
            userPassMapping.remove(username);
            return pw;
        } else {
            Logger.getLogger(AccountVerifierBean.class.getName()).log(Level.WARNING, null, new Exception("Corresponding username for " + username + " could not be found."));
            return null;
        }
    }
    public String generateKey(String username) {
        Date time = new Date();
        String key = username + time.toString();
        key = Encryption.hashSHAHex(key);
        if(key == null) {
            Logger.getLogger(CustomerService.class.getName())
                    .log(Level.SEVERE, null, new Exception("Key Hash Failed."));
            return null;
        }
        if(verificationKeyMapping.containsKey(username)) {
            verificationKeyMapping.remove(username);
        }
        verificationKeyMapping.put(username, key);
        return key;
    }

    public void setPassword(String username, String password) {
        if(userPassMapping.containsKey(username)) {
            userPassMapping.remove(username);
        }
        password = Encryption.hashSHABase64(password);
        userPassMapping.put(username, password);
    }
}
