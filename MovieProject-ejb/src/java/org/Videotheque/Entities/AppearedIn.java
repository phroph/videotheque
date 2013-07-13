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
@Table(name = "AppearedIn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppearedIn.findAll", query = "SELECT a FROM AppearedIn a"),
    @NamedQuery(name = "AppearedIn.findByActorId", query = "SELECT a FROM AppearedIn a WHERE a.appearedInPK.actorId = :actorId"),
    @NamedQuery(name = "AppearedIn.findByMovieId", query = "SELECT a FROM AppearedIn a WHERE a.appearedInPK.movieId = :movieId")})
public class AppearedIn implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AppearedInPK appearedInPK;

    public AppearedIn() {
    }

    public AppearedIn(AppearedInPK appearedInPK) {
        this.appearedInPK = appearedInPK;
    }

    public AppearedIn(int actorId, String movieId) {
        this.appearedInPK = new AppearedInPK(actorId, movieId);
    }

    public AppearedInPK getAppearedInPK() {
        return appearedInPK;
    }

    public void setAppearedInPK(AppearedInPK appearedInPK) {
        this.appearedInPK = appearedInPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appearedInPK != null ? appearedInPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppearedIn)) {
            return false;
        }
        AppearedIn other = (AppearedIn) object;
        if ((this.appearedInPK == null && other.appearedInPK != null) || (this.appearedInPK != null && !this.appearedInPK.equals(other.appearedInPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.AppearedIn[ appearedInPK=" + appearedInPK + " ]";
    }
    
}
