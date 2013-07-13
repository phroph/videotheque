/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Phillip
 */
@Embeddable
public class MovieQPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "AccountId")
    private int accountId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "MovieId")
    private String movieId;

    public MovieQPK() {
    }

    public MovieQPK(int accountId, String movieId) {
        this.accountId = accountId;
        this.movieId = movieId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) accountId;
        hash += (movieId != null ? movieId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieQPK)) {
            return false;
        }
        MovieQPK other = (MovieQPK) object;
        if (this.accountId != other.accountId) {
            return false;
        }
        if ((this.movieId == null && other.movieId != null) || (this.movieId != null && !this.movieId.equals(other.movieId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.MovieQPK[ accountId=" + accountId + ", movieId=" + movieId + " ]";
    }
    
}