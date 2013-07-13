/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.io.IOException;
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
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.Videotheque.Entities.Account;
import org.Videotheque.Entities.Actor;
import org.Videotheque.Entities.AppearedIn;
import org.Videotheque.Entities.AppearedInPK;
import org.Videotheque.Entities.Customer;
import org.Videotheque.Entities.DBCustomer;
import org.Videotheque.Entities.DBEmployee;
import org.Videotheque.Entities.Employee;
import org.Videotheque.Entities.Groups;
import org.Videotheque.Entities.Movie;
import org.Videotheque.Entities.MovieQ;
import org.Videotheque.Entities.MovieQPK;
import org.Videotheque.Entities.Orders;
import org.Videotheque.Entities.Person;
import org.Videotheque.Entities.Rental;
import org.Videotheque.Entities.RentalPK;
import org.Videotheque.Entities.UserGroup;
import org.Videotheque.Entities.UserGroupPK;
import org.Videotheque.utils.ImageUtils;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
@ViewScoped
@TransactionManagement(TransactionManagementType.BEAN)
public class AdminService {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public static final int PAGE_SIZE = 3;
    @Resource
    SessionContext context;
    private boolean showManagerPane = false;
    private boolean showAdminPane = false;
    private boolean showRepresentativePane = false;
    private boolean showMoviePane = false;
    private boolean showEmployeePane = false;
    private boolean showCustomerPane = false;
    private boolean showOrderPane = false;
    private boolean showAddExistingActorPane = false;
    private int moviePage = 0;
    private int employeePage = 0;
    private int customerPage = 0;
    private int customerId = -1;
    private int movieId = -1;
    private int employeeId = -1;
    private int orderId = -1;
    private int actorId = -1;
    private int actorMovieId = -1;
    
    private int actorExistingActorId;
    private String actorName;
    private String actorAge;
    private char actorGender;
    private String actorDescription;
    private String actorImageURL;
    private String customerType;
    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;
    private String customerZipcode;
    private String customerTelephone;
    private String customerGroup;
    private String employeeHourlyRate;
    private String employeeLastName;
    private String employeeFirstName;
    private String employeeAddress;
    private String employeeZipcode;
    private String employeeTelephone;
    private String movieName;
    private String movieDescription;
    private String movieDistFee;
    private String movieNumCopies;
    private String movieType;
    private String movieImageURL;
    private String rentalMovieId;
    private String rentalAccountId;
    private String orderTitle;
    private String orderEmail;
    private String orderEmployeeId;
    
    UIComponent order;
    
    private List<Movie> movieCache;
    private List<Employee> employeeCache;
    private List<Customer> customerCache;
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;
    @EJB
    private UserService userService;

    public void invalidateMovieCache() {
        movieCache = null;
    }

    public void invalidateEmployeeCache() {
        employeeCache = null;
    }

    public void invalidateCustomerCache() {
        customerCache = null;
    }
    
    public List<Movie> getCustomerSuggestions(int id) {
        List<Movie> retVal = new ArrayList<>();
        List<Integer> results = em.createNativeQuery("call getSuggestions(:customerId)")
                .setParameter("customerId",id).getResultList();
        for(Integer i : results) {
            try{
                Movie m = (Movie)em.createNamedQuery("Movie.findById").setParameter("id", i).getSingleResult();
                if(m != null) {
                    retVal.add(m);
                }
            }
            catch(Exception e) {
                Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return retVal;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }
    public List<String> getMailingList() {
        return em.createNamedQuery("Customer.getMailingList").getResultList();
    }

    public List<Movie> getCurrentMoviePage() {
        if (!isMovieCacheValid()) {
            generateMovieCache();
        }
        if (!maxMoviePage()) {
            return movieCache.subList(moviePage * PAGE_SIZE, moviePage * PAGE_SIZE + PAGE_SIZE);
        } else {
            return movieCache.subList(moviePage * PAGE_SIZE, movieCache.size());
        }
    }

    public List<Employee> getCurrentEmployeePage() {
        if (!isEmployeeCacheValid()) {
            generateEmployeeCache();
        }
        if (!maxEmployeePage()) {
            return employeeCache.subList(employeePage * PAGE_SIZE, employeePage * PAGE_SIZE + PAGE_SIZE);
        } else {
            return employeeCache.subList(employeePage * PAGE_SIZE, employeeCache.size());
        }
    }

    public List<Customer> getCurrentCustomerPage() {
        if (!isCustomerCacheValid()) {
            generateCustomerCache();
        }
        if (!maxCustomerPage()) {
            return customerCache.subList(customerPage * PAGE_SIZE, customerPage * PAGE_SIZE + PAGE_SIZE);
        } else {
            return customerCache.subList(customerPage * PAGE_SIZE, customerCache.size());
        }
    }

    public void nextMoviePage() {
        if (moviePage < (int) (movieCache.size() / PAGE_SIZE)) {
            moviePage++;
        }
    }

    public void prevMoviePage() {
        if (moviePage > 0) {
            moviePage--;
        }
    }

    public void nextCustomerPage() {
        if (customerPage < (int) (customerCache.size() / PAGE_SIZE)) {
            customerPage++;
        }
    }

    public void prevCustomerPage() {
        if (customerPage > 0) {
            customerPage--;
        }
    }

    public void nextEmployeePage() {
        if (employeePage < (int) (employeeCache.size() / PAGE_SIZE)) {
            employeePage++;
        }
    }

    public void prevEmployeePage() {
        if (employeePage > 0) {
            employeePage--;
        }
    }

    public boolean maxMoviePage() {
        return moviePage >= (int) ((movieCache.size() - 1) / PAGE_SIZE);
    }

    public boolean minMoviePage() {
        return moviePage <= 0;
    }

    public boolean maxCustomerPage() {
        return customerPage >= (int) ((customerCache.size() - 1) / PAGE_SIZE);
    }

    public boolean minCustomerPage() {
        return customerPage <= 0;
    }

    public boolean maxEmployeePage() {
        return employeePage >= (int) ((employeeCache.size() - 1) / PAGE_SIZE);
    }

    public boolean minEmployeePage() {
        return employeePage <= 0;
    }

    public void generateCustomerCache() {
        customerCache = new LinkedList<>(em.createNamedQuery("Customer.findAll").getResultList());
    }

    public void generateEmployeeCache() {
        employeeCache = new LinkedList<>(em.createNamedQuery("Employee.findAll").getResultList());
    }

    public void generateMovieCache() {
        movieCache = new LinkedList<>(em.createNamedQuery("Movie.findAll").getResultList());
    }

    public String getEmployeeTelephone() {
        return employeeTelephone;
    }

    public String getActorDescription() {
        return actorDescription;
    }

    public void setActorDescription(String actorDescription) {
        this.actorDescription = actorDescription;
    }

    public String getActorImageURL() {
        return actorImageURL;
    }

    public void setActorImageURL(String actorImageURL) {
        this.actorImageURL = actorImageURL;
    }

    public UIComponent getOrder() {
        return order;
    }

    public void setOrder(UIComponent order) {
        this.order = order;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }

    public String getOrderEmployeeId() {
        return orderEmployeeId;
    }

    public void setOrderEmployeeId(String orderEmployeeId) {
        this.orderEmployeeId = orderEmployeeId;
    }

    public void setEmployeeTelephone(String employeeTelephone) {
        this.employeeTelephone = employeeTelephone;
    }

    public boolean isShowOrderPane() {
        return showOrderPane;
    }

    public void setShowOrderPane(boolean showOrderPane) {
        this.showOrderPane = showOrderPane;
    }

    public boolean isCustomerCacheValid() {
        return customerCache != null;
    }

    public boolean isEmployeeCacheValid() {
        return employeeCache != null;
    }

    public boolean isMovieCacheValid() {
        return movieCache != null;
    }

    public boolean isShowMoviePane() {
        return showMoviePane;
    }

    public boolean isShowCustomerPane() {
        return showCustomerPane;
    }

    public int getActorExistingActorId() {
        return actorExistingActorId;
    }

    public void setActorExistingActorId(int actorExistingActorId) {
        this.actorExistingActorId = actorExistingActorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorAge() {
        return actorAge;
    }

    public void setActorAge(String actorAge) {
        this.actorAge = actorAge;
    }

    public char getActorGender() {
        return actorGender;
    }

    public void setActorGender(char actorGender) {
        this.actorGender = actorGender;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerZipcode() {
        return customerZipcode;
    }

    public void setCustomerZipcode(String customerZipcode) {
        this.customerZipcode = customerZipcode;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public boolean isShowAddExistingActorPane() {
        return showAddExistingActorPane;
    }

    public void setShowAddExistingActorPane(boolean showAddExistingActorPane) {
        this.showAddExistingActorPane = showAddExistingActorPane;
    }

    public int getActorMovieId() {
        return actorMovieId;
    }

    public void setActorMovieId(int actorMovieId) {
        this.actorMovieId = actorMovieId;
    }

    public String getEmployeeHourlyRate() {
        return employeeHourlyRate;
    }

    public void setEmployeeHourlyRate(String employeeHourlyRate) {
        this.employeeHourlyRate = employeeHourlyRate;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeZipcode() {
        return employeeZipcode;
    }

    public void setEmployeeZipcode(String employeeZipcode) {
        this.employeeZipcode = employeeZipcode;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieDistFee() {
        return movieDistFee;
    }

    public void setMovieDistFee(String movieDistFee) {
        this.movieDistFee = movieDistFee;
    }

    public String getMovieNumCopies() {
        return movieNumCopies;
    }

    public void setMovieNumCopies(String movieNumCopies) {
        this.movieNumCopies = movieNumCopies;
    }

    public String getMovieImageURL() {
        return movieImageURL;
    }

    public void setMovieImageURL(String movieImageURL) {
        this.movieImageURL = movieImageURL;
    }

    public String getRentalMovieId() {
        return rentalMovieId;
    }

    public void setRentalMovieId(String rentalMovieId) {
        this.rentalMovieId = rentalMovieId;
    }

    public String getRentalAccountId() {
        return rentalAccountId;
    }

    public void setRentalAccountId(String rentalAccountId) {
        this.rentalAccountId = rentalAccountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public void setShowCustomerPane(boolean showCustomerPane) {
        this.showCustomerPane = showCustomerPane;
    }

    public void setShowMoviePane(boolean showMoviePane) {
        this.showMoviePane = showMoviePane;
    }

    public boolean isShowEmployeePane() {
        return showEmployeePane;
    }

    public void setShowEmployeePane(boolean showEmployeePane) {
        this.showEmployeePane = showEmployeePane;
    }

    public boolean isShowRepresentativePane() {
        return showRepresentativePane;
    }

    public void setShowRepresentativePane(boolean showRepresentativePane) {
        this.showRepresentativePane = showRepresentativePane;
    }

    public boolean isShowManagerPane() {
        return showManagerPane;
    }

    public void setShowManagerPane(boolean showManagerPane) {
        this.showManagerPane = showManagerPane;
    }

    public boolean isShowAdminPane() {
        return showAdminPane;
    }

    public void setShowAdminPane(boolean showAdminPane) {
        this.showAdminPane = showAdminPane;
    }

    public void toggleManagerPane() {
        showManagerPane = true;
        showAdminPane = false;
        showRepresentativePane = false;

        showMoviePane = false;
        showEmployeePane = false;

        showCustomerPane = false;
        showOrderPane = false;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleAdminPane() {
        showAdminPane = true;
        showManagerPane = false;
        showRepresentativePane = false;

        showMoviePane = false;
        showEmployeePane = false;

        showCustomerPane = false;
        showOrderPane = false;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleRepresentativePane() {
        showAdminPane = false;
        showManagerPane = false;
        showRepresentativePane = true;

        showMoviePane = false;
        showEmployeePane = false;

        showCustomerPane = false;
        showOrderPane = false;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleMoviePane() {
        showMoviePane = true;
        showEmployeePane = false;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleEmployeePane() {
        showMoviePane = false;
        showEmployeePane = true;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleCustomerPane() {
        showCustomerPane = true;
        showOrderPane = false;
        
        showAddExistingActorPane = false;
        

        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }

    public void toggleOrderPane() {
        orderEmail = "";
        orderEmployeeId = "";
        orderTitle = "";
        
        showCustomerPane = false;
        showOrderPane = true;

        showAddExistingActorPane = false;
        
        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }
    public void toggleAddExistingActorPane() {
        showAddExistingActorPane = true;
    }

    public boolean hasAdminPrivileges() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null
                && userService.getUserRole().equals(Groups.ADMIN_GROUP)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasRepresentativePrivileges() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null
                && (userService.getUserRole().equals(Groups.ADMIN_GROUP)
                || userService.getUserRole().equals(Groups.MANAGER_GROUP)
                || userService.getUserRole().equals(Groups.REPRESENTATIVE_GROUP))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasManagerPrivileges() {
        if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null
                && (userService.getUserRole().equals(Groups.ADMIN_GROUP)
                || userService.getUserRole().equals(Groups.MANAGER_GROUP))) {
            return true;
        } else {
            return false;
        }
    }

    public void addEmployee() {
        Person p = new Person();
        DBEmployee e = new DBEmployee();
        e.setHourlyRate(0);
        e.setStartDate(new Date());
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(e);
            em.refresh(e);
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
        }
        e.setSsn(e.getId());
        p.setAddress("");
        p.setFirstName("");
        p.setLastName("");
        p.setTelephone(0);
        p.setZipCode(0);
        p.setSsn(e.getSsn());
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(p);
            em.merge(e);
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
        }
        invalidateEmployeeCache();
    }

    public void addMovie() {
        Movie m = new Movie();
        m.setDescription("");
        m.setDistrFee(0);
        m.setName("");
        m.setNumCopies(0);
        m.setRating(0);
        m.setType("");
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(m);
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
        }
        invalidateMovieCache();
    }

    public void addCustomer() {
        Account a = new Account();
        DBCustomer c = new DBCustomer();
        Person p = new Person();
        UserGroup g = new UserGroup();
        UserGroupPK gPK = new UserGroupPK();

        a.setEmail("");
        a.setPassword("");
        a.setDateOpened(new Date());
        a.setType("limited");
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(a);
            em.refresh(a); //Refresh our account so we can access the generated ID
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
        } // ... really now Oracle?

        a.setCustomer(a.getId());
        a.setEmail(Integer.toString(a.getId()));
        gPK.setUserId(a.getId());
        gPK.setGroupId(Groups.USER_GROUPID); //Assign our user to the USER group
        g.setId(gPK);
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(g); //Persist our user to the USER group
            em.merge(a); //Merge our changes into the database
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
        } // ... really now Oracle?

        c.setId(a.getId());
        c.setRating(0);
        p.setFirstName("");
        p.setLastName("");
        p.setZipCode(0);
        p.setSsn(c.getId());
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(c);
            em.persist(p);
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
        } // ... really now Oracle?


        invalidateCustomerCache();
    }
    
    public void addExistingActor(int movieId) {
        toggleAddExistingActorPane();
        actorMovieId = movieId;
    }
    public void saveExistingActor(Movie movie){
        int id = actorExistingActorId;
        AppearedIn ain = new AppearedIn();
        AppearedInPK ainPK = new AppearedInPK();
        ainPK.setActorId(id);
        ainPK.setMovieId(Integer.toString(movie.getId()));
        ain.setAppearedInPK(ainPK);
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(ain);
            ut.commit();
            movie.addActor((Actor)em.createNamedQuery("Actor.findById").setParameter("id", id).getSingleResult());
        } catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
        }
        cancelEdit();
    }
    public void addActor() {
        
    }

    public void addOrder() {
        Orders o = new Orders();
        o.setDateTime(new Date());
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(o);
            em.refresh(o);
            ut.commit();
        } catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
            FacesMessage msg = new FacesMessage("Order placement insuccessful.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(order.getClientId(FacesContext.getCurrentInstance()), msg);
            return;
        }
        Rental r = new Rental();
        RentalPK rPK = new RentalPK();
        Customer c = (Customer) em.createNamedQuery("Customer.findByEmail").setParameter("email", orderEmail).getSingleResult();
        Employee e = (Employee) em.createNamedQuery("Employee.findById").setParameter("id", Integer.parseInt(orderEmployeeId)).getSingleResult();
        Movie m = (Movie) em.createNamedQuery("Movie.findByName").setParameter("name", orderTitle).getSingleResult();
        rPK.setAccountId(c.getId());
        rPK.setOrderId(o.getId());
        rPK.setMovieId(Integer.toString(m.getId()));
        rPK.setCustRepId(e.getId());
        r.setRentalPK(rPK);
        MovieQ q = new MovieQ();
        MovieQPK qPK = new MovieQPK();
        qPK.setAccountId(c.getId());
        qPK.setMovieId(Integer.toString(m.getId()));
        q.setMovieQPK(qPK);
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(r);
            em.persist(q);
            ut.commit();
        } catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
            FacesMessage msg = new FacesMessage("Order placement insuccessful.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(order.getClientId(FacesContext.getCurrentInstance()), msg);
            return;
        }
        FacesMessage msg = new FacesMessage("Order successfully placed.");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(order.getClientId(FacesContext.getCurrentInstance()), msg);            
        toggleOrderPane();
    }

    public void authorize() throws IOException {
        if(userService.getUserRole().equals(Groups.USER_GROUP)) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/MovieProject/forbidden.xhtml");
        }
    }
    public void saveCustomer(Customer customer) {
        Person p = customer.getPerson();
        Account a = customer.getAccount();
        p.setAddress(customerAddress);
        p.setFirstName(customerFirstName);
        p.setLastName(customerLastName);
        p.setTelephone(Integer.parseInt(customerTelephone));
        p.setZipCode(Integer.parseInt(customerZipcode));
        a.setEmail(customerEmail);
        a.setType(customerType);
        Groups g = Groups.getGroup(customerGroup);
        UserGroup ug = (UserGroup) em.createNamedQuery("UserGroup.findByUserId").setParameter("id", customer.getId()).getSingleResult();
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.remove(em.merge(ug));
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
        } // ... really now Oracle?
        catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Miscellaneous Exception", err);
        }

        ug = new UserGroup();
        UserGroupPK ugPK = new UserGroupPK();
        ugPK.setGroupId(g.getId());
        ugPK.setUserId(customer.getId());
        ug.setId(ugPK);
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(ug);
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
        } // ... really now Oracle?

        customer.setGroup(g);
        customer.setAddress(customerAddress);
        customer.setFirstName(customerFirstName);
        customer.setLastName(customerLastName);
        customer.setZipCode(Integer.parseInt(customerZipcode));
        customer.setEmail(customerEmail);
        customer.setType(customerType);
        customer.setTelephone(Integer.parseInt(customerTelephone));
        cancelEdit();
    }

    public void saveEmployee(Employee employee) {
        Person p = employee.getPerson();
        DBEmployee e = employee.getEmployee();
        e.setHourlyRate(Integer.parseInt(employeeHourlyRate));
        p.setFirstName(employeeFirstName);
        p.setLastName(employeeLastName);
        p.setTelephone(Integer.parseInt(employeeTelephone));
        p.setZipCode(Integer.parseInt(employeeZipcode));
        p.setAddress(employeeAddress);
        employee.setHourlyRate(Integer.parseInt(employeeHourlyRate));
        employee.setFirstName(employeeFirstName);
        employee.setLastName(employeeLastName);
        employee.setTelephone(Integer.parseInt(employeeTelephone));
        employee.setZipCode(Integer.parseInt(employeeZipcode));
        employee.setAddress(employeeAddress);
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.merge(p);
            em.merge(e);
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
        } // ... really now Oracle?

        employee.setFirstName(employeeFirstName);
        employee.setLastName(employeeLastName);
        employee.setAddress(employeeAddress);
        employee.setHourlyRate(Integer.parseInt(employeeHourlyRate));
        employee.setZipCode(Integer.parseInt(employeeZipcode));
        cancelEdit();
    }

    public void saveActor(Actor actor) {
    }

    public void saveOrder(Orders order) {
    }

    public void saveMovie(Movie movie) {
        try {
            if (movieImageURL != null && !movieImageURL.isEmpty()) {
                ImageUtils.saveMovieImage(movieImageURL, movieName);
                movie.setImageId(movieName.replace(" ", "") + ".png");
            }
        } catch (Exception e) {
            Logger.getLogger(AdminService.class.getName()).log(Level.WARNING, "Image Upload Exception", e);
        }
        movie.setDescription(movieDescription);
        movie.setDistrFee(Integer.parseInt(movieDistFee));
        movie.setNumCopies(Integer.parseInt(movieNumCopies));
        movie.setName(movieName);
        movie.setType(movieType);
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.merge(movie);
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
        } // ... really now Oracle?
        //downloadimage and set imageURL

        cancelEdit();
    }

    public void editMovie(Movie movie) {
        movieId = movie.getId();
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;

        this.movieImageURL = "";
        this.movieType = movie.getType();
        this.movieDescription = movie.getDescription();
        this.movieDistFee = movie.getDistrFee() != null ? Integer.toString(movie.getDistrFee()) : "0";
        this.movieName = movie.getName();
        this.movieNumCopies = movie.getNumCopies() != null ? Integer.toString(movie.getNumCopies()) : "0";
    }

    public void editOrder(Orders order) {
        movieId = -1;
        customerId = -1;
        orderId = order.getId();
        actorId = -1;
        employeeId = -1;
        actorMovieId = -1;
    }

    public void editActor(Actor actor, Movie movie) {
        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = actor.getId();
        employeeId = -1;
        actorMovieId = movie.getId();
        actorAge = Integer.toString(actor.getAge());
        actorName = actor.getName();
        actorGender = actor.getGender();
        actorDescription = actor.getDescription();
    }

    public void editEmployee(Employee employee) {
        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = employee.getId();
        actorMovieId = -1;
        employeeZipcode = employee.getZipCode() != null ? Integer.toString(employee.getZipCode()) : "0";
        employeeAddress = employee.getAddress();
        employeeFirstName = employee.getFirstName();
        employeeLastName = employee.getLastName();
        employeeTelephone = employee.getTelephone() != null ? Integer.toString(employee.getTelephone()) : "0";
        employeeHourlyRate = employee.getHourlyRate() != null ? Integer.toString(employee.getHourlyRate()) : "0";
    }

    public void editCustomer(Customer customer) {
        movieId = -1;
        customerId = customer.getId();
        orderId = -1;
        actorId = -1;
        employeeId = -1;
        actorMovieId = -1;
        customerEmail = customer.getEmail();
        customerFirstName = customer.getFirstName();
        customerLastName = customer.getLastName();
        customerTelephone = customer.getTelephone() != null ? Integer.toString(customer.getTelephone()) : "0";
        customerZipcode = customer.getZipCode() != null ? Integer.toString(customer.getZipCode()) : "0";
        customerAddress = customer.getAddress();
        customerType = customer.getType();
        customerGroup = customer.getGroup().getName();
    }

    public void cancelEdit() {
        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
        actorMovieId = -1;
    }

    public String getSuggestionURL(int id) {
        return "/Admin/customersuggestions?faces-redirect=true&user=" + id;
    }

    public List<String> getUserGroups() {
        List<String> retVal = new LinkedList<>();
        retVal.add(Groups.USER_GROUP);
        retVal.add(Groups.REPRESENTATIVE_GROUP);
        retVal.add(Groups.MANAGER_GROUP);
        return retVal;
    }

    public List<String> getUserTypes() {
        List<String> retVal = new LinkedList<>();
        retVal.add("free");
        retVal.add("limited");
        retVal.add("unlimited-1");
        retVal.add("unlimited-2");
        retVal.add("unlimited-3");
        retVal.add("employee");
        return retVal;
    }

    public void preparePage() {
        showAdminPane = false;
        showManagerPane = false;
        showRepresentativePane = false;

        showMoviePane = false;
        showEmployeePane = false;

        showCustomerPane = false;
        showOrderPane = false;

        customerPage = 0;
        employeePage = 0;
        moviePage = 0;

        movieId = -1;
        customerId = -1;
        orderId = -1;
        actorId = -1;
        employeeId = -1;
    }
    public void deleteActor(Actor a, Movie m) {
        
    }
    public void deleteMovie(Movie m) {
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            Movie movie = em.find(Movie.class, m.getId());
            em.remove(em.merge(movie));
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
        } // ... really now Oracle?
        invalidateMovieCache();
        toggleMoviePane();
    }

    public void deleteCustomer(Customer c) {
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            Account a = em.find(Account.class, c.getId());
            if (a != null) {
                em.remove(em.merge(a));
            }
            Person p = em.find(Person.class, c.getSsn());
            if (p != null) {
                em.remove(em.merge(p));
            }
            DBCustomer dbc = em.find(DBCustomer.class, c.getSsn());
            if (dbc != null) {
                em.remove(em.merge(dbc));
            }
            try {
                UserGroup ug = c.getUserGroup();
                if (ug != null) {
                    em.remove(em.merge(ug));
                }
            } catch (NoResultException err) {
                Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, err);
            }
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
        } // ... really now Oracle?
        invalidateCustomerCache();
        toggleCustomerPane();
    }

    public void deleteEmployee(Employee e) {
        if(e.getId() == 7 && e.getFirstName().equals("INTERNET") && e.getLastName().equals("ADMIN")) {
            return;
        }
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            DBEmployee emp = em.find(DBEmployee.class, e.getId());
            Person p = em.find(Person.class, e.getSsn());
            em.remove(em.merge(emp));
            em.remove(em.merge(p));
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
        } // ... really now Oracle?
        invalidateEmployeeCache();
        toggleEmployeePane();
    }
    public Long getNumFreeAccounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumFreeAccounts").setParameter("date", reference).getSingleResult();
    }
    public Long getNumLimitedAccounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumLimitedAccounts").setParameter("date", reference).getSingleResult();
    }
    public Long getNumUnlimited1Accounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumUnlimited1Accounts").setParameter("date", reference).getSingleResult();
    }
    public Long getNumUnlimited2Accounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumUnlimited2Accounts").setParameter("date", reference).getSingleResult();
    }
    public Long getNumUnlimited3Accounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumUnlimited3Accounts").setParameter("date", reference).getSingleResult();
    }
    public Long getNumEmployeeAccounts() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        Date reference = cal.getTime();
        return (Long) em.createNamedQuery("Customer.getNumEmployeeAccounts").setParameter("date", reference).getSingleResult();
    }
    public Long getSalesReport() {
        return (getNumUnlimited1Accounts() * 15) + (getNumLimitedAccounts() * 10) + (getNumUnlimited2Accounts() * 20) + (getNumUnlimited3Accounts() * 25); 
    }
    public List<Movie> getMostActiveMovies() {
        List<Movie> retVal = new ArrayList<>();
        List<Integer> results = em.createNativeQuery("call getActiveMovies()").getResultList();
        for(Integer i : results) {
            try{
                Movie m = (Movie)em.createNamedQuery("Movie.findById").setParameter("id", i).getSingleResult();
                if(m != null) {
                    retVal.add(m);
                }
            }
            catch(Exception e) {
                Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return retVal;
    }
    public List<Employee> getMostActiveEmployees() {
        List<Employee> retVal = new ArrayList<>();
        List<Integer> results = em.createNativeQuery("call getActiveEmployees()").getResultList();
        for(Integer i : results) {
            try{
                Employee m = (Employee)em.createNamedQuery("Employee.findById").setParameter("id", i).getSingleResult();
                if(m != null) {
                    retVal.add(m);
                }
            }
            catch(Exception e) {
                Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return retVal;
    }
    public List<Customer> getMostActiveCustomers() {
        List<Customer> retVal = new ArrayList<>();
        List<Integer> results = em.createNativeQuery("call getActiveCustomers()").getResultList();
        for(Integer i : results) {
            try{
                Customer m = (Customer)em.createNamedQuery("Customer.findById").setParameter("id", i).getSingleResult();
                if(m != null) {
                    retVal.add(m);
                }
            }
            catch(Exception e) {
                Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return retVal;
    }
    public boolean isValidEmail(String str) {
        return str.contains("@");
    }
}
