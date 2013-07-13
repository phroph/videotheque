/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.Videotheque.utils.EntityManagerFactoryManager;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "CustomerAccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = :id"),
    @NamedQuery(name = "Customer.findByDateOpened", query = "SELECT c FROM Customer c WHERE c.dateOpened = :dateOpened"),
    @NamedQuery(name = "Customer.findByType", query = "SELECT c FROM Customer c WHERE c.type = :type"),
    @NamedQuery(name = "Customer.findByEmail", query = "SELECT c FROM Customer c WHERE c.email = :email"),
    @NamedQuery(name = "Customer.findByRating", query = "SELECT c FROM Customer c WHERE c.rating = :rating"),
    @NamedQuery(name = "Customer.findByCreditCardNumber", query = "SELECT c FROM Customer c WHERE c.creditCardNumber = :creditCardNumber"),
    @NamedQuery(name = "Customer.findBySsn", query = "SELECT c FROM Customer c WHERE c.ssn = :ssn"),
    @NamedQuery(name = "Customer.findByFirstName", query = "SELECT c FROM Customer c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Customer.findByLastName", query = "SELECT c FROM Customer c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Customer.findByAddress", query = "SELECT c FROM Customer c WHERE c.address = :address"),
    @NamedQuery(name = "Customer.findByZipCode", query = "SELECT c FROM Customer c WHERE c.zipCode = :zipCode"),
    @NamedQuery(name = "Customer.findByCity", query = "SELECT c FROM Customer c WHERE c.city = :city"),
    @NamedQuery(name = "Customer.findByState", query = "SELECT c FROM Customer c WHERE c.state = :state"),
    @NamedQuery(name = "Customer.findByTelephone", query = "SELECT c FROM Customer c WHERE c.telephone = :telephone"),
    @NamedQuery(name = "Customer.getRentalHistory", query= "SELECT m FROM Movie m, Customer c, Rental r, Orders o WHERE c.id = :id AND r.rentalPK.orderId = o.id AND r.rentalPK.movieId = m.id AND r.rentalPK.accountId = c.id"),
    @NamedQuery(name = "Customer.getRentalHistoryDates", query= "SELECT o.dateTime FROM Movie m, Customer c, Rental r, Orders o WHERE c.id = :id AND r.rentalPK.orderId = o.id AND r.rentalPK.movieId = m.id AND r.rentalPK.accountId = c.id"),
    @NamedQuery(name = "Customer.getMailingList", query="SELECT c.email FROM Customer c WHERE c.email IS NOT NULL"),
    @NamedQuery(name = "Customer.getNumLimitedAccounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'limited' AND c.dateOpened < :date"),
    @NamedQuery(name = "Customer.getNumUnlimited1Accounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'unlimited-1' AND c.dateOpened < :date"),
    @NamedQuery(name = "Customer.getNumUnlimited2Accounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'unlimited-2' AND c.dateOpened < :date"),
    @NamedQuery(name = "Customer.getNumUnlimited3Accounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'unlimited-3' AND c.dateOpened < :date"),
    @NamedQuery(name = "Customer.getNumFreeAccounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'free' AND c.dateOpened < :date"),
    @NamedQuery(name = "Customer.getNumEmployeeAccounts", query="SELECT COUNT(c) FROM Customer c WHERE c.type = 'employee' AND c.dateOpened < :date")
})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Transient
    private EntityManagerFactory emf = EntityManagerFactoryManager.emf;
    @Id
    @Column(name = "Id")
    private Integer id;
    @Column(name = "DateOpened")
    @Temporal(TemporalType.DATE)
    private Date dateOpened;
    @Size(max = 20)
    @Column(name = "Type")
    private String type;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 32)
    @Column(name = "Email")
    private String email;
    @Column(name = "Rating")
    private Integer rating;
    @Column(name = "CreditCardNumber")
    private Integer creditCardNumber;
    @Column(name = "SSN")
    private Integer ssn;
    @Size(max = 20)
    @Column(name = "FirstName")
    private String firstName;
    @Size(max = 20)
    @Column(name = "LastName")
    private String lastName;
    @Size(max = 20)
    @Column(name = "Address")
    private String address;
    @Column(name = "ZipCode")
    private Integer zipCode;
    @Size(max = 20)
    @Column(name = "City")
    private String city;
    @Size(max = 20)
    @Column(name = "State")
    private String state;
    @Column(name = "Telephone")
    private Integer telephone;
    @Column()
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="MovieQ", 
        joinColumns=@JoinColumn(name="AccountId", referencedColumnName="Id"),
        inverseJoinColumns=@JoinColumn(name="MovieId", referencedColumnName="Id"))
    private List<Movie> movieQ;
    @ManyToOne
    @JoinTable(name="UserGroup",
        joinColumns = @JoinColumn(name = "UserId", 
                              referencedColumnName = "Id"), 
        inverseJoinColumns = @JoinColumn(name = "GroupId", 
                              referencedColumnName = "Id"))
    private Groups group;
    public Customer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Movie> getMovieQ() {
        return movieQ;
    }

    public void setMovieQ(List<Movie> movieQ) {
        this.movieQ = movieQ;
    }
    
    public Date getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(Date dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getType() {
        return type;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(Integer creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    public List<Movie> getSuggestions() {
        EntityManager em = emf.createEntityManager();
        List<Movie> retVal = new ArrayList<Movie>();
        List<Integer> results = em.createNativeQuery("call getSuggestions(:customerId)")
                .setParameter("customerId",this.id).getResultList();
        for(Integer i : results) {
            try{
                Movie m = (Movie)em.createNamedQuery("Movie.findById").setParameter("id", i).getSingleResult();
                if(m != null) {
                    retVal.add(m);
                }
            }
            catch(Exception e) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return retVal;
    }
    public List<Movie> getRentalHistory() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNamedQuery("Customer.getRentalHistory");
        q.setParameter("id", getId());
        List<Movie> retVal = q.getResultList();
        return retVal;
    }
    public Integer getSsn() {
        return ssn;
    }

    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }
    public DBCustomer getCustomer() {
        EntityManager em = emf.createEntityManager();
        return (DBCustomer) em.createNamedQuery("DBCustomer.findById").setParameter("id", ssn).getSingleResult();
    }
    public Person getPerson() {
        EntityManager em = emf.createEntityManager();
        return (Person) em.createNamedQuery("Person.findBySsn").setParameter("ssn", ssn).getSingleResult();        
    }
    public Account getAccount() {
        EntityManager em = emf.createEntityManager();
        return (Account) em.createNamedQuery("Account.findById").setParameter("id", id).getSingleResult();
    }
    public UserGroup getUserGroup() {
        EntityManager em = emf.createEntityManager();
        return (UserGroup) em.createNamedQuery("UserGroup.findByUserId").setParameter("id", id).getSingleResult();
    }
}
