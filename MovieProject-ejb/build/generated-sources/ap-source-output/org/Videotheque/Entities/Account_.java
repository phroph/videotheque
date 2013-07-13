package org.Videotheque.Entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:04")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, Integer> id;
    public static volatile SingularAttribute<Account, Date> dateOpened;
    public static volatile SingularAttribute<Account, String> email;
    public static volatile SingularAttribute<Account, String> type;
    public static volatile SingularAttribute<Account, Integer> customer;
    public static volatile SingularAttribute<Account, String> password;

}