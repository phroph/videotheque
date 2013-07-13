package org.Videotheque.Entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.Videotheque.Entities.Groups;
import org.Videotheque.Entities.Movie;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:03")
@StaticMetamodel(Customer.class)
public class Customer_ { 

    public static volatile SingularAttribute<Customer, String> lastName;
    public static volatile ListAttribute<Customer, Movie> movieQ;
    public static volatile SingularAttribute<Customer, String> state;
    public static volatile SingularAttribute<Customer, Integer> ssn;
    public static volatile SingularAttribute<Customer, String> type;
    public static volatile SingularAttribute<Customer, String> city;
    public static volatile SingularAttribute<Customer, Integer> id;
    public static volatile SingularAttribute<Customer, Date> dateOpened;
    public static volatile SingularAttribute<Customer, String> email;
    public static volatile SingularAttribute<Customer, String> address;
    public static volatile SingularAttribute<Customer, Integer> zipCode;
    public static volatile SingularAttribute<Customer, Integer> creditCardNumber;
    public static volatile SingularAttribute<Customer, Integer> rating;
    public static volatile SingularAttribute<Customer, String> firstName;
    public static volatile SingularAttribute<Customer, Groups> group;
    public static volatile SingularAttribute<Customer, Integer> telephone;

}