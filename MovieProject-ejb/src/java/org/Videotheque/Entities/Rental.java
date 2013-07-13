/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "Rental")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rental.findAll", query = "SELECT r FROM Rental r"),
    @NamedQuery(name = "Rental.findByAccountId", query = "SELECT r FROM Rental r WHERE r.rentalPK.accountId = :accountId"),
    @NamedQuery(name = "Rental.findByCustRepId", query = "SELECT r FROM Rental r WHERE r.rentalPK.custRepId = :custRepId"),
    @NamedQuery(name = "Rental.findByOrderId", query = "SELECT r FROM Rental r WHERE r.rentalPK.orderId = :orderId"),
    @NamedQuery(name = "Rental.findByMovieId", query = "SELECT r FROM Rental r WHERE r.rentalPK.movieId = :movieId"),
    @NamedQuery(name = "Rental.findUnreturnedMovieByIds", query = "SELECT o FROM Rental r, Orders o WHERE r.rentalPK.orderId = o.id AND o.returnDate IS NULL AND r.rentalPK.movieId = :movieid AND r.rentalPK.accountId = :userid")})
public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RentalPK rentalPK;

    public Rental() {
    }

    public Rental(RentalPK rentalPK) {
        this.rentalPK = rentalPK;
    }

    public Rental(int accountId, int custRepId, int orderId, String movieId) {
        this.rentalPK = new RentalPK(accountId, custRepId, orderId, movieId);
    }

    public RentalPK getRentalPK() {
        return rentalPK;
    }

    public void setRentalPK(RentalPK rentalPK) {
        this.rentalPK = rentalPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalPK != null ? rentalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rental)) {
            return false;
        }
        Rental other = (Rental) object;
        if ((this.rentalPK == null && other.rentalPK != null) || (this.rentalPK != null && !this.rentalPK.equals(other.rentalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.Rental[ rentalPK=" + rentalPK + " ]";
    }
    
}
