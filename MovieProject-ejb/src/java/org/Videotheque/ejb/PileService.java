/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.Videotheque.Entities.Customer;
import org.Videotheque.Entities.Movie;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
public class PileService {
    private static final int PAGE_SIZE = 4;
    private static final String ALL_MOVIES = "ALL";
    private static final String SUGGESTED_MOVIES = "SUGGEST";
    private static final String QUEUED_MOVIES = "QUEUE";
    private static final String BEST_MOVIES = "BEST";
    private static final String USER_PILE = "USER";
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;
    private List<String> movieImages;
    private List<String> queueImages;
    private List<String> bestImages;
    private List<String> suggestImages;
    private List<String> userImages;
    
    private List<Movie> movies;
    private List<Movie> queueMovies;
    private List<Movie> bestMovies;
    private List<Movie> suggestMovies;
    
    private String movieTitle;
    private String movieDescription;
    private int movieId = -1;
    private int page = 0;
    private float rating;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private String pileId = BEST_MOVIES;
    @PostConstruct
    public void setupCaches() {
        String pileReserve = pileId;
        
        bestImages = new ArrayList<>();
        queueImages = new ArrayList<>();
        suggestImages = new ArrayList<>();
        movieImages = new ArrayList<>();
        userImages = new ArrayList<>();
        bestMovies = new ArrayList<>();
        queueMovies = new ArrayList<>();
        suggestMovies = new ArrayList<>();
        movies = new ArrayList<>();
        setMoviesPile();
        List<Movie> movies = getPileContents();
        this.movies.addAll(movies);
        if(movies != null) {
            for(Movie m : movies) {
                if(m != null) {
                    movieImages.add(m.getImageId());
                }
            }
        }
        setBestsellerPile();
        movies = getPileContents();
        bestMovies.addAll(movies);
        if(movies != null) {
            for(Movie m : movies) {
                if(m != null) {
                    bestImages.add(m.getImageId());
                }
            }
        }
        setQueuePile();
        movies = getPileContents();
        queueMovies.addAll(movies);
        if(movies != null) {
            for(Movie m : movies) {
                if(m != null) {
                    queueImages.add(m.getImageId());
                }
            }
        }
        setSuggestionPile();
        movies = getPileContents();
        suggestMovies.addAll(movies);
        if(movies != null) {
            for(Movie m : movies) {
                if(m != null) {
                    suggestImages.add(m.getImageId());
                }
            }
        }
        pileId = pileReserve;
    }
    public void setMoviesPile() {
        pileId = ALL_MOVIES;
        page = 0;
    }
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public void setQueuePile() {
        pileId = QUEUED_MOVIES;
        page = 0;
    }

    public List<String> getMovieImages() {
        return movieImages;
    }

    public void setMovieImages(List<String> movieImages) {
        this.movieImages = movieImages;
    }

    public List<String> getQueueImages() {
        return queueImages;
    }

    public void setQueueImages(List<String> queueImages) {
        this.queueImages = queueImages;
    }

    public List<String> getUserImages() {
        return userImages;
    }

    public void setUserImages(List<String> userImages) {
        this.userImages = userImages;
    }

    public List<String> getBestImages() {
        return bestImages;
    }

    public void setBestImages(List<String> bestImages) {
        this.bestImages = bestImages;
    }

    public List<String> getSuggestImages() {
        return suggestImages;
    }

    public void setSuggestImages(List<String> suggestImages) {
        this.suggestImages = suggestImages;
    }
    
    public void setBestsellerPile() {
        pileId = BEST_MOVIES;
        page = 0;
    }
    public void setSuggestionPile() {
        pileId = SUGGESTED_MOVIES;
        page = 0;
    }
    public String getPileName() {
        if(pileId.equals(ALL_MOVIES)) {
            return "Movies";
        } else if(pileId.equals(SUGGESTED_MOVIES)) {
            return "Suggested for You";
        } else if(pileId.equals(QUEUED_MOVIES)) {
            return "My Queue";
        } else if(pileId.equals(BEST_MOVIES)) {
            return "Best-sellers";
        } else {
            return "User-defined Movies";
        }
    }
    public List<Movie> getMovieList() {
        switch (pileId) {
            case ALL_MOVIES:
                return movies;
            case QUEUED_MOVIES:
                return queueMovies;
            case BEST_MOVIES:
                return bestMovies;
            case SUGGESTED_MOVIES:
                return suggestMovies;
            default:
                return movies;
        }
    }
    public List<Movie> getPage() {
        if (!isMaxPage()) {
            return getMovieList().subList(page * PAGE_SIZE, page * PAGE_SIZE + PAGE_SIZE);
        } else {
            return getMovieList().subList(page * PAGE_SIZE, getMovieList().size());
        }
    }
    public boolean isMaxPage() {
        return page >= (int) ((getMovieList().size() - 1) / PAGE_SIZE);
    }
    public boolean isMinPage() {
        return page <= 0;
    }
    public void getNextPage() {
        if(!isMaxPage()) {
            page++;
        }
    }
    public void getPreviousPage() {
        if(!isMinPage()) {
            page--;
        }
    }
    public List<Movie> getPileContents() {
        List<Movie> retVal = new ArrayList<>();
        if(pileId.equals(ALL_MOVIES)) {
            Query q = em.createNamedQuery("Movie.findAll");
            for(Object mov : q.getResultList()) {
                retVal.add((Movie)mov);
            }
        } else if(pileId.equals(QUEUED_MOVIES)) {
            String username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            Query q = em.createNamedQuery("Customer.findByEmail");
            q.setParameter("email", username);
            try {
                Customer c = (Customer)q.getSingleResult();
                retVal.addAll(c.getMovieQ());
            } catch (Exception e) {
                Logger.getLogger(PileService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if(pileId.equals(BEST_MOVIES)) {
            Query q = em.createNamedQuery("Movie.findBestsellers");
            q.setMaxResults(10);
            try {
                for(Object mov : q.getResultList()) {
                    retVal.add((Movie)mov);
                }
            } catch (Exception e) {
                Logger.getLogger(PileService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if(pileId.equals(SUGGESTED_MOVIES)) {
            String username = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            Query q = em.createNamedQuery("Customer.findByEmail");
            q.setParameter("email", username);
            try {
                Customer c = (Customer)q.getSingleResult();
                retVal.addAll(c.getSuggestions());
            } catch (Exception e) {
                Logger.getLogger(PileService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            //get by genre pileId.
        }
        return retVal;
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    public void loadMovieData(Movie m) {
        if(m == null) {
            Logger.getLogger(PileService.class.getName()).log(Level.SEVERE, null, new NullPointerException());
            return;
        }
        rating = m.getRating();
        movieDescription = m.getDescription();
        movieTitle = m.getName();
        movieId = m.getId();
    }
    public void clearMovieData() {
        movieId = -1;
    }
}
