/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.Videotheque.Entities.Actor;
import org.Videotheque.Entities.Movie;
import org.Videotheque.utils.MovieValueTuple;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
public class SearchService {
    public static final String[] skipWords = {"the","of","and","or","a","an"};
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    private String query;
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public List<Movie> getSearchResults() {
        String[] tokens = query.split(" ");
        List<Movie> movies = em.createNamedQuery("Movie.findAll").getResultList();
        HashSet<String> removeDups = new HashSet<>();
        for(String s : tokens) {
            boolean skip = false;
            for(String skipWord : skipWords) {
                if(skipWord.equalsIgnoreCase(s)) {
                    skip = true;
                    break;
                }
            }
            if(!skip) {
                removeDups.add(s);
            }
        }
        List<String> searchTokens = new LinkedList<>();
        for(String s : removeDups) {
            searchTokens.add(s);
        }
        PriorityQueue<MovieValueTuple> returnList = new PriorityQueue<>();
        for(Movie m : movies) {
            MovieValueTuple movieValue = new  MovieValueTuple(m,0);
            for(String s : searchTokens) {
                if(m.getName().toLowerCase().contains(s.toLowerCase())) {
                    movieValue.two(movieValue.two()+2);
                }
                for(Actor a : m.getActors()) {
                    if(a.getName().toLowerCase().contains(s.toLowerCase())) {
                        movieValue.two(movieValue.two()+1);
                    }
                }
                if(m.getType().toLowerCase().contains(s.toLowerCase())) {
                    movieValue.two(movieValue.two()+1);
                }
            }
            if(movieValue.two()>0) {
                returnList.offer(movieValue);
            }
        }
        List<Movie> retVal = new LinkedList<>();
        for(MovieValueTuple mvt : returnList) {
            retVal.add(mvt.one());
        }
        return retVal;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
