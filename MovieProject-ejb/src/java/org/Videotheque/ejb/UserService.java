/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
import org.Videotheque.Entities.Location;
import org.Videotheque.Entities.Movie;
import org.Videotheque.Entities.Person;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class UserService {

    @Resource
    SessionContext context;
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;
    private UIComponent errorLabel;
    private UIInput confirmPassword;
    private UIInput ccn;
    @EJB
    private PileService pileService;
    @EJB
    private AdminService adminService;
    private String username;
    private String password;
    private String userRole;
    private String userType;
    private String editAddress;
    private String editCCN;
    private String editAccountType;
    private String editZipcode;
    private String editCity;
    private String editState;
    private String editTelephone;
    private String editPassword;
    private String editVerifyPassword;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public UIInput getConfirmPassword() {
        return confirmPassword;
    }

    public String getEditAddress() {
        return editAddress;
    }

    public void setEditAddress(String editAddress) {
        this.editAddress = editAddress;
    }

    public UIInput getCcn() {
        return ccn;
    }

    public void setCcn(UIInput ccn) {
        this.ccn = ccn;
    }

    public String getEditCCN() {
        return editCCN;
    }

    public void setEditCCN(String editCCN) {
        this.editCCN = editCCN;
    }

    public String getEditAccountType() {
        return editAccountType;
    }

    public void setEditAccountType(String editAccountType) {
        this.editAccountType = editAccountType;
    }

    public String getEditZipcode() {
        return editZipcode;
    }

    public void setEditZipcode(String editZipcode) {
        this.editZipcode = editZipcode;
    }

    public String getEditCity() {
        return editCity;
    }

    public void setEditCity(String editCity) {
        this.editCity = editCity;
    }

    public String getEditState() {
        return editState;
    }

    public void setEditState(String editState) {
        this.editState = editState;
    }

    public String getEditTelephone() {
        return editTelephone;
    }

    public void setEditTelephone(String editTelephone) {
        this.editTelephone = editTelephone;
    }

    public String getEditPassword() {
        return editPassword;
    }

    public void setEditPassword(String editPassword) {
        this.editPassword = editPassword;
    }

    public String getEditVerifyPassword() {
        return editVerifyPassword;
    }

    public void setEditVerifyPassword(String editVerifyPassword) {
        this.editVerifyPassword = editVerifyPassword;
    }

    public void setConfirmPassword(UIInput confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UIComponent getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(UIComponent errorLabel) {
        this.errorLabel = errorLabel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    public void setUsername(String un) {
        username = un;
    }

    public String getLoginOutcome() {
        if (isUserLoggedIn()) {
            if (getUserRole().equals(Groups.ADMIN_GROUP) || getUserRole().equals(Groups.MANAGER_GROUP) || getUserRole().equals(Groups.REPRESENTATIVE_GROUP)) {
                return "/Admin/admin?faces-redirect=true";
            } else {
                return "/User/user?faces-redirect=true";
            }
        } else {
            return "/login";
        }
    }

    public String getLoginName() {
        if (isUserLoggedIn()) {
            return getUserRole() + " Page";
        } else {
            return "Sign-In";
        }
    }

    public String getCreateAccountOutcome() {
        if (isUserLoggedIn()) {
            logout();
            return "/index.xhtml?faces-redirect=true";
        } else {
            return "/create.xhtml";
        }
    }

    public String getCreateAccountName() {
        if (isUserLoggedIn()) {
            return "Logout";
        } else {
            return "Sign-Up";
        }
    }

    public String login() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            request.login(username.toLowerCase(), password);
            invalidateUserRole();
            invalidateUserType();
            pileService.setupCaches();
        } catch (ServletException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage msg = new FacesMessage("Username or password is incorrect.");
            msg.setSeverity(FacesMessage.SEVERITY_FATAL);
            FacesContext.getCurrentInstance().addMessage(errorLabel.getClientId(FacesContext.getCurrentInstance()), msg);
            return "/login";
        }
        String username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        try {
            Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username.toLowerCase()).getSingleResult();
            if (c.getGroup().getName().equals(Groups.ADMIN_GROUP)) {
                adminService.preparePage();
                return "/Admin/admin?faces-redirect=true";
            }
        } catch (Exception e) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Something unexpected happened getting User from Principal", e);
        }
        return "/User/movies?faces-redirect=true";
    }

    public boolean isUserLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
    }

    public void logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            request.logout();
            invalidateUserRole();
            invalidateUserType();
        } catch (ServletException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void invalidateUserRole() {
        userRole = null;
    }

    public void invalidateUserType() {
        userType = null;
    }

    public String getLogoNavigationCase() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
            return "/index?faces-redirect=true";
        } else {
            return "/User/movies?faces-redirect-true";
        }
    }

    public String getUserType() {
        if (userType == null || userType.equals("")) {
            Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
            if (principal == null) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, new Exception("User principal not found."));
                return "";
            }
            String username = principal.getName();
            Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username).getSingleResult();
            userRole = c.getGroup().getName();
            userType = c.getType();
            return userType;
        } else {
            return userType;
        }
    }

    public String getUserRole() {
        if (userRole == null || userRole.equals("")) {
            Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
            if (principal == null) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, new Exception("User principal not found."));
                return "";
            }
            String username = principal.getName();
            Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username).getSingleResult();
            userRole = c.getGroup().getName();
            userType = c.getType();
            return userRole;
        } else {
            return userRole;
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public boolean checkEmail(String email) {
        try {
            em.createNamedQuery("Customer.findByEmail").setParameter("email", email).getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

    public int getMovieQueueSize() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (principal == null) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, new Exception("User principal not found."));
            return 0;
        }
        String username = principal.getName();
        Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username).getSingleResult();
        return c.getMovieQ().size();
    }

    public int getNumMoviesRentedThisMonth() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (principal == null) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, new Exception("User principal not found."));
            return 0;
        }
        String username = principal.getName();
        Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username).getSingleResult();
        List<Date> movies = em.createNamedQuery("Customer.getRentalHistoryDates").setParameter("id", c.getId()).getResultList();
        int i = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        for (Date d : movies) {
            if (d.after(reference)) {
                i++;
            }
        }
        return i;
    }

    public Customer getCustomer() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (principal == null) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, new Exception("User principal not found."));
            return null;
        }
        String username = principal.getName();
        return (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", username).getSingleResult();
    }

    public void editAccount() {
        Customer c = getCustomer();
        DBCustomer cust = c.getCustomer();
        Person p = c.getPerson();
        Account a = c.getAccount();
        Location l = new Location();
        if (editAddress != null && !editAddress.isEmpty()) {
            c.setAddress(editAddress);
            p.setAddress(editAddress);
        }
        if (editZipcode != null && !editZipcode.isEmpty()) {
            try {
                Integer zc = Integer.parseInt(editZipcode);
                p.setZipCode(zc);
                p.setZipCode(zc);
                UserTransaction ut = context.getUserTransaction();
                try {
                    
                    ut.begin();
                    ((Location) em.find(Location.class, zc)).getCity();
                    ut.commit();
                } catch (Exception e) {
                    l.setCity(editCity);
                    l.setState(editState);
                    l.setZipCode(zc);
                    ut.rollback();
                    ut = context.getUserTransaction();
                    try {
                        ut.begin();
                        em.persist(l);
                        ut.commit();
                    } catch (NotSupportedException err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Transaction Not Supported", err);
                    } catch (RollbackException err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
                    } catch (SystemException err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "System Exception", err);
                    } catch (HeuristicMixedException err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Heuristic Exception", err);
                    } catch (HeuristicRollbackException err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
                    } catch (Exception err) {
                        Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
                    }
                }
            } catch (Exception e) {
            }
        }
        if (editTelephone != null && !editTelephone.isEmpty()) {
            try {
                Integer tp = Integer.parseInt(editTelephone);
                c.setTelephone(tp);
                p.setTelephone(tp);
            } catch (Exception e) {
            }
        }
        if (editCCN != null && !editCCN.isEmpty()) {
            try {
                Integer ccn = Integer.parseInt(editCCN);
                c.setCreditCardNumber(ccn);
                cust.setCreditCardNumber(ccn);
            } catch (Exception e) {
            }
        }
        if (editAccountType != null && !editAccountType.isEmpty()) {
            String type = editAccountType.split(" ")[0];
            c.setType(type);
            a.setType(type);
        }
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.merge(cust);
            em.merge(p);
            em.merge(a);
            ut.commit();
        } catch (NotSupportedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Transaction Not Supported", err);
        } catch (RollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } catch (SystemException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "System Exception", err);
        } catch (HeuristicMixedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Heuristic Exception", err);
        } catch (HeuristicRollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
        }
    }

    public List<String> getAccountTypes() {
        List<String> retVal = new LinkedList<>();
        retVal.add("free - $0/mo.");
        retVal.add("limited - $10/mo. (1 movie at a time, 2 a month)");
        retVal.add("unlimited-1 - $15/mo. (1 movie at a time)");
        retVal.add("unlimited-2 - $20/mo. (2 movies at a time)");
        retVal.add("unlimited-3 - $25/mo. (3 movies at a time)");
        return retVal;
    }

    public List<Movie> getMovieHistory() {
        return getCustomer().getRentalHistory();
    }
}
