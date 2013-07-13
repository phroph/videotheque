package org.Videotheque.Entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.Videotheque.Entities.Actor;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-05-09T03:15:03")
@StaticMetamodel(Movie.class)
public class Movie_ { 

    public static volatile SingularAttribute<Movie, Integer> id;
    public static volatile SingularAttribute<Movie, String> imageId;
    public static volatile SingularAttribute<Movie, String> description;
    public static volatile SingularAttribute<Movie, String> name;
    public static volatile SetAttribute<Movie, Actor> actors;
    public static volatile SingularAttribute<Movie, Integer> rating;
    public static volatile SingularAttribute<Movie, Integer> distrFee;
    public static volatile SingularAttribute<Movie, String> type;
    public static volatile SingularAttribute<Movie, Integer> numCopies;

}