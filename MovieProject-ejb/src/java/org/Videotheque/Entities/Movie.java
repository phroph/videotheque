/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "Movie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id"),
    @NamedQuery(name = "Movie.findByName", query = "SELECT m FROM Movie m WHERE m.name = :name"),
    @NamedQuery(name = "Movie.findByType", query = "SELECT m FROM Movie m WHERE m.type = :type"),
    @NamedQuery(name = "Movie.findByRating", query = "SELECT m FROM Movie m WHERE m.rating = :rating"),
    @NamedQuery(name = "Movie.findByDistrFee", query = "SELECT m FROM Movie m WHERE m.distrFee = :distrFee"),
    @NamedQuery(name = "Movie.findBestsellers", query = "SELECT m FROM Rental r, Movie m WHERE r.rentalPK.movieId = m.id GROUP BY m.id ORDER BY COUNT(m) DESC"),
    @NamedQuery(name = "Movie.findByNumCopies", query = "SELECT m FROM Movie m WHERE m.numCopies = :numCopies")})
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Name")
    private String name;
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Type")
    private String type;
    @Column(name = "Rating")
    private Integer rating;
    @Column(name = "DistrFee")
    private Integer distrFee;
    @Column(name = "NumCopies")
    private Integer numCopies;
    @Column(name = "ImageId")
    private String imageId;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="AppearedIn", 
        joinColumns=@JoinColumn(name="MovieId", referencedColumnName="Id"),
        inverseJoinColumns=@JoinColumn(name="ActorId", referencedColumnName="Id"))
    private Set<Actor> actors;
    public Movie() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Actor> getActors() {
        List<Actor> retVal = new LinkedList<>();
        for(Actor a : actors) {
            retVal.add(a);
        }
        return retVal;
    }
    
    public void addActor(Actor a) {
        actors.add(a);
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Movie(Integer id) {
        this.id = id;
    }

    public Movie(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getDistrFee() {
        return distrFee;
    }

    public void setDistrFee(Integer distrFee) {
        this.distrFee = distrFee;
    }

    public Integer getNumCopies() {
        return numCopies;
    }

    public void setNumCopies(Integer numCopies) {
        this.numCopies = numCopies;
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
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.Movie[ id=" + id + " ]";
    }
    
}
