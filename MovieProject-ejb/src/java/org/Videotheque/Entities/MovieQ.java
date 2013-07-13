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
@Table(name = "MovieQ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovieQ.findAll", query = "SELECT m FROM MovieQ m"),
    @NamedQuery(name = "MovieQ.findByAccountId", query = "SELECT m FROM MovieQ m WHERE m.movieQPK.accountId = :accountId"),
    @NamedQuery(name = "MovieQ.findByMovieId", query = "SELECT m FROM MovieQ m WHERE m.movieQPK.movieId = :movieId"),
    @NamedQuery(name = "MovieQ.findByPK", query = "SELECT m FROM MovieQ m WHERE m.movieQPK.accountId = :accountid AND m.movieQPK.movieId = :movieid")})
public class MovieQ implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovieQPK movieQPK;

    public MovieQ() {
    }

    public MovieQ(MovieQPK movieQPK) {
        this.movieQPK = movieQPK;
    }

    public MovieQ(int accountId, String movieId) {
        this.movieQPK = new MovieQPK(accountId, movieId);
    }

    public MovieQPK getMovieQPK() {
        return movieQPK;
    }

    public void setMovieQPK(MovieQPK movieQPK) {
        this.movieQPK = movieQPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movieQPK != null ? movieQPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieQ)) {
            return false;
        }
        MovieQ other = (MovieQ) object;
        if ((this.movieQPK == null && other.movieQPK != null) || (this.movieQPK != null && !this.movieQPK.equals(other.movieQPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.MovieQ[ movieQPK=" + movieQPK + " ]";
    }
    
}
