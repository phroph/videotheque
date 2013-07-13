package org.Videotheque.Entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:03")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, Integer> id;
    public static volatile SingularAttribute<Employee, String> lastName;
    public static volatile SingularAttribute<Employee, Date> startDate;
    public static volatile SingularAttribute<Employee, Integer> hourlyRate;
    public static volatile SingularAttribute<Employee, String> address;
    public static volatile SingularAttribute<Employee, Integer> zipCode;
    public static volatile SingularAttribute<Employee, Integer> ssn;
    public static volatile SingularAttribute<Employee, String> state;
    public static volatile SingularAttribute<Employee, String> firstName;
    public static volatile SingularAttribute<Employee, Integer> telephone;
    public static volatile SingularAttribute<Employee, String> city;

}