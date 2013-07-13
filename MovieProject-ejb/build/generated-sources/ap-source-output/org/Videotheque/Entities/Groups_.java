package org.Videotheque.Entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.Videotheque.Entities.Customer;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:03")
@StaticMetamodel(Groups.class)
public class Groups_ { 

    public static volatile SingularAttribute<Groups, Integer> id;
    public static volatile ListAttribute<Groups, Customer> users;
    public static volatile SingularAttribute<Groups, String> name;

}