package org.Videotheque.Entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:03")
@StaticMetamodel(DBEmployee.class)
public class DBEmployee_ { 

    public static volatile SingularAttribute<DBEmployee, Integer> id;
    public static volatile SingularAttribute<DBEmployee, Date> startDate;
    public static volatile SingularAttribute<DBEmployee, Integer> hourlyRate;
    public static volatile SingularAttribute<DBEmployee, Integer> ssn;

}