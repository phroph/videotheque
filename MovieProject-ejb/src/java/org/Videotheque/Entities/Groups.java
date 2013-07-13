/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.Videotheque.utils.EntityManagerFactoryManager;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "Groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g"),
    @NamedQuery(name = "Groups.findById", query = "SELECT g FROM Groups g WHERE g.id = :id"),
    @NamedQuery(name = "Groups.findByName", query = "SELECT g FROM Groups g WHERE g.name = :name")})
public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private static  EntityManagerFactory emf = EntityManagerFactoryManager.emf;  
    public static final String ADMIN_GROUP = "Admin";
    public static final String USER_GROUP = "User";
    public static final String REPRESENTATIVE_GROUP = "Employee";
    public static final String MANAGER_GROUP = "Manager";
    public static final int ADMIN_GROUPID = 0;
    public static final int USER_GROUPID = 1;
    public static final int MANAGER_GROUPID = 2;
    public static final int REPRESENTATIVE_GROUPID = 3;


    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "Name")
    private String name;
    @OneToMany(mappedBy="group")
    List<Customer> users;

    public Groups() {
    }

    public Groups(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.Groups[ id=" + id + " ]";
    }
    public static Groups getGroup(String name) {
        if(name.equals(Groups.REPRESENTATIVE_GROUP)) {
            return (Groups)emf.createEntityManager().createNamedQuery("Groups.findByName").setParameter("name", name).getSingleResult();
        }
        else if(name.equals(Groups.MANAGER_GROUP)) {
            return (Groups)emf.createEntityManager().createNamedQuery("Groups.findByName").setParameter("name", name).getSingleResult();           
        }
        else if(name.equals(Groups.ADMIN_GROUP)) {
            return (Groups)emf.createEntityManager().createNamedQuery("Groups.findByName").setParameter("name", name).getSingleResult();
        }
        else {
            return (Groups)emf.createEntityManager().createNamedQuery("Groups.findByName").setParameter("name", Groups.USER_GROUP).getSingleResult();

        }
    }    
}
