/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.Videotheque.Entities.Account;
import org.Videotheque.Entities.Customer;
import org.Videotheque.Entities.DBCustomer;
import org.Videotheque.Entities.Groups;
import org.Videotheque.Entities.Movie;
import org.Videotheque.Entities.MovieQ;
import org.Videotheque.Entities.MovieQPK;
import org.Videotheque.Entities.Person;
import org.Videotheque.Entities.UserGroup;
import org.Videotheque.Entities.UserGroupPK;
import org.Videotheque.beans.AccountVerifierBean;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
public class CustomerService {
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;
    
    
    
    private UIComponent errorLabel;
    
    private String username;
    private String password;
    
    @EJB
    private UserService userService;

    public UIComponent getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(UIComponent errorLabel) {
        this.errorLabel = errorLabel;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void persist(Object object) {
        em.persist(object);
    }
    public String verifyKey(String username, String key) {
        AccountVerifierBean avBean = (AccountVerifierBean) FacesContext.getCurrentInstance()
                .getExternalContext().getApplicationMap().get("accountVerifierBean");
        if(avBean.verifyKey(username.toLowerCase(), key)) {
            String password = avBean.getPassword(username.toLowerCase());
            addUser(username.toLowerCase(), password);
            return "Verification successful, you may now log in to VideoPile!";
        } else {
            String expected = avBean.getExpectedKey(username.toLowerCase());
            if(expected == null) {
                expected = "<no key exists>";
            }
            Logger.getLogger(CustomerService.class.getName()).log(Level.WARNING, null, new Exception("Key mismatch: " + key + " given, " + expected + " expected." ));
            return "Verification failed, please try creating an account again.";
        }
    }
    
    public String validateEmail() {
        AccountVerifierBean avBean = (AccountVerifierBean) FacesContext.getCurrentInstance()
                .getExternalContext().getApplicationMap().get("accountVerifierBean");
        Properties serverConfig = new Properties();
        serverConfig.put("mail.smtp.host", "localhost");
        serverConfig.put("mail.smtp.auth", "true");
        serverConfig.put("mail.smtp.port", "25");
        
        try {
            Session session = Session.getInstance(serverConfig, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("accounts@minimalcomputers.com","cheeseman2");
                }
            });
            MimeMessage message = new MimeMessage( session );
            message.setFrom(new InternetAddress("accounts@minimalcomputers.com","VideoPile"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(username));
            message.setSubject("Welcome to VideoPile!");
            avBean.setPassword(username.toLowerCase(), password);
            String key = avBean.generateKey(username.toLowerCase());
            if(key == null) {
                FacesMessage msg = new FacesMessage("There was an error sending your validation email. Please try again");
                msg.setSeverity(FacesMessage.SEVERITY_FATAL);
                FacesContext.getCurrentInstance().addMessage(errorLabel.getClientId(FacesContext.getCurrentInstance()), msg);
                return null;
            }
            String link = "http://www.minimalcomputers.com:8080/MovieProject/verify.xhtml?user="
                    + username.toLowerCase() + "&key=" + key;
            String content = "<p>Welcome to VideoPile, Please verify your email by clicking the following link:</p><p><a href=\""
                    + link + "\">" + link + "</a></p>";
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send( message );
            FacesMessage msg = new FacesMessage("Verification email send to your email address!");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(errorLabel.getClientId(FacesContext.getCurrentInstance()), msg);
            return null;
        }
        catch (MessagingException ex){
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage msg = new FacesMessage("There was an error sending your validation email. Please try again");
            msg.setSeverity(FacesMessage.SEVERITY_FATAL);
            FacesContext.getCurrentInstance().addMessage(errorLabel.getClientId(FacesContext.getCurrentInstance()), msg);
            return null;
        }
        catch (Exception e) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, e);
            FacesMessage msg = new FacesMessage("There was an error sending your validation email. Please try again");
            msg.setSeverity(FacesMessage.SEVERITY_FATAL);
            FacesContext.getCurrentInstance().addMessage(errorLabel.getClientId(FacesContext.getCurrentInstance()), msg);
            return null;
        }
    }
    public void addUser(String username, String password) {
        Account a = new Account();
        DBCustomer c = new DBCustomer();
        Person p = new Person();
        UserGroup g = new UserGroup();
        UserGroupPK gPK = new UserGroupPK();
        
        {   //Scopes added for cleanliness
            a.setEmail(username.toLowerCase());
            a.setPassword(password);
            a.setDateOpened(new Date());
            a.setType("free");
        }
        persist(a);
        em.refresh(a); //Refresh our account so we can access the generated ID
        a.setCustomer(a.getId());
        
        gPK.setUserId(a.getId());
        gPK.setGroupId(Groups.USER_GROUPID); //Assign our user to the USER group
        g.setId(gPK);
        persist(g); //Persist our user to the USER group
        em.merge(a); //Merge our changes into the database
        
        {
            c.setId(a.getId());  
            c.setRating(0);
        }
        persist(c);
        
        {
            p.setFirstName("");
            p.setLastName("");
            p.setZipCode(0);
            p.setSsn(c.getId());
        }
        persist(p);
    }
}
