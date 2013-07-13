/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Phillip
 */
@Entity
@Table(name = "Employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DBEmployee.findAll", query = "SELECT d FROM DBEmployee d"),
    @NamedQuery(name = "DBEmployee.findById", query = "SELECT d FROM DBEmployee d WHERE d.id = :id"),
    @NamedQuery(name = "DBEmployee.findBySsn", query = "SELECT d FROM DBEmployee d WHERE d.ssn = :ssn"),
    @NamedQuery(name = "DBEmployee.findByStartDate", query = "SELECT d FROM DBEmployee d WHERE d.startDate = :startDate"),
    @NamedQuery(name = "DBEmployee.findByHourlyRate", query = "SELECT d FROM DBEmployee d WHERE d.hourlyRate = :hourlyRate")})
public class DBEmployee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "SSN")
    private Integer ssn;
    @Column(name = "StartDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "HourlyRate")
    private Integer hourlyRate;

    public DBEmployee() {
    }

    public DBEmployee(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSsn() {
        return ssn;
    }

    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
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
        if (!(object instanceof DBEmployee)) {
            return false;
        }
        DBEmployee other = (DBEmployee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.DBEmployee[ id=" + id + " ]";
    }
    
}
