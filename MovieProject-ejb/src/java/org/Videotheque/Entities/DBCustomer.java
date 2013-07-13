/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "Customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DBCustomer.findAll", query = "SELECT d FROM DBCustomer d"),
    @NamedQuery(name = "DBCustomer.findById", query = "SELECT d FROM DBCustomer d WHERE d.id = :id"),
    @NamedQuery(name = "DBCustomer.findByRating", query = "SELECT d FROM DBCustomer d WHERE d.rating = :rating"),
    @NamedQuery(name = "DBCustomer.findByCreditCardNumber", query = "SELECT d FROM DBCustomer d WHERE d.creditCardNumber = :creditCardNumber")})
public class DBCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Rating")
    private Integer rating;
    @Column(name = "CreditCardNumber")
    private Integer creditCardNumber;

    public DBCustomer() {
    }

    public DBCustomer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBCustomer)) {
            return false;
        }
        DBCustomer other = (DBCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.DBCustomer[ id=" + id + " ]";
    }
    
}
