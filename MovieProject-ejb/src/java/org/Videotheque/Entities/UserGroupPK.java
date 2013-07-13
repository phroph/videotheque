package org.Videotheque.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class UserGroupPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserId")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GroupId")
    private int groupId;
    
    public UserGroupPK() {
    }

    public UserGroupPK(int userId, int groupId) {
        this.userId = groupId;
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) groupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof org.Videotheque.Entities.RentalPK)) {
            return false;
        }
        org.Videotheque.Entities.UserGroupPK other = (org.Videotheque.Entities.UserGroupPK) object;
        if (this.groupId != other.userId) {
            return false;
        }
        if (this.groupId != other.groupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.Videotheque.Entities.UserGroupPK[ accountId=" + userId + ", custRepId=" + groupId + " ]";
    }
    
}