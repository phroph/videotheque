/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.Videotheque.Entities.Actor;
import org.Videotheque.Entities.Customer;
import org.Videotheque.Entities.Movie;
import org.Videotheque.Entities.MovieQ;
import org.Videotheque.Entities.MovieQPK;
import org.Videotheque.Entities.Orders;
import org.Videotheque.Entities.Rental;
import org.Videotheque.Entities.RentalPK;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class MovieService {

    Movie movie;
    Actor actor;
    @Resource
    SessionContext context;
    @EJB
    UserService userService;
    @EJB
    PileService pileService;
    
    @PersistenceContext(unitName = "MovieProject-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie m) {
        this.movie = m;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void loadMovie(int id) {
        //if(id == 0) {
        //    return;
        //}
        movie = null;
        try {
            //int id = Integer.parseInt(((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getParameter("id"));
            movie = (Movie) em.createNamedQuery("Movie.findById").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(MovieService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadActor() {
        actor = null;
        try {
            int id = Integer.parseInt(((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getParameter("id"));
            actor = (Actor) em.createNamedQuery("Actor.findById").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(MovieService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getActorName() {
        if (actor == null) {
            return "Actor Page";
        } else {
            return actor.getName();
        }
    }

    public String getMovieTitle() {
        if (movie == null) {
            return "Movie Page";
        } else {
            return movie.getName();
        }
    }

    public void addToMovieQueue() {
        
        Customer c = userService.getCustomer();
        MovieQ q = new MovieQ();
        MovieQPK qPK = new MovieQPK();
        qPK.setAccountId(c.getId());
        qPK.setMovieId(Integer.toString(movie.getId()));
        q.setMovieQPK(qPK);
        Rental r = new Rental();
        RentalPK rPK = new RentalPK();
        Orders o = new Orders();
        o.setDateTime(new Date());
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(o);
            em.refresh(o);
            ut.commit();
            pileService.setupCaches();
        } catch (NotSupportedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Transaction Not Supported", err);
        } catch (RollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } catch (SystemException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "System Exception", err);
        } catch (HeuristicMixedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Heuristic Exception", err);
        } catch (HeuristicRollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } // ... really now Oracle?
        catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Misc. Exception", err);
        }
        rPK.setOrderId(o.getId());
        rPK.setCustRepId(7); // 7 is ID for INTERNET ADMIN, the transactions handler for online transactions.
        rPK.setAccountId(c.getId());
        rPK.setMovieId(Integer.toString(movie.getId()));
        r.setRentalPK(rPK);
        ut = context.getUserTransaction();
        try {
            ut.begin();
            em.persist(q);
            em.persist(r);
            em.refresh(em.merge(c));
            ut.commit();
            pileService.setupCaches();
        } catch (NotSupportedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Transaction Not Supported", err);
        } catch (RollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } catch (SystemException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "System Exception", err);
        } catch (HeuristicMixedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Heuristic Exception", err);
        } catch (HeuristicRollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } // ... really now Oracle?
        catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Misc. Exception", err);
        }
    }

    public boolean canAddToMovieQueue() {
        String type = userService.getUserType();
        if (!userService.isUserLoggedIn() || movie == null) {
            return false;
        }
        if (userService.getCustomer().getMovieQ().contains(movie)) {
            return false;
        }
        switch (type) {
            case "employee":
                return true;
            case "unlimited-1":
                return userService.getMovieQueueSize() < 1;
            case "unlimited-2":
                return userService.getMovieQueueSize() < 2;
            case "unlimited-3":
                return userService.getMovieQueueSize() < 3;
            case "limited":
                return userService.getMovieQueueSize() < 1
                        && userService.getNumMoviesRentedThisMonth() < 2;
            default:
                return false;
        }
    }
    public boolean canReturnMovie() {
        if (!userService.isUserLoggedIn() || movie == null) {
            return false;
        }
        int id = userService.getCustomer().getId();
        try {
            Orders orders = (Orders) em.createNamedQuery("Rental.findUnreturnedMovieByIds").setParameter("movieid", Integer.toString(movie.getId())).setParameter("userid", id).getSingleResult();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    public void returnMovie() {
        Customer customer = userService.getCustomer();
        int id = customer.getId();
        Orders order = (Orders) em.createNamedQuery("Rental.findUnreturnedMovieByIds").setParameter("movieid", Integer.toString(movie.getId())).setParameter("userid", customer.getId()).getSingleResult();
        order.setReturnDate(new Date());
        MovieQPK moviePK = new MovieQPK();
        moviePK.setAccountId(customer.getId());
        moviePK.setMovieId(Integer.toString(movie.getId()));
        UserTransaction ut = context.getUserTransaction();
        try {
            ut.begin();
            em.remove(em.find(MovieQ.class, moviePK));
            em.merge(order);
            ut.commit();
            pileService.setupCaches();
        } catch (NotSupportedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Transaction Not Supported", err);
        } catch (RollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } catch (SystemException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "System Exception", err);
        } catch (HeuristicMixedException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Heuristic Exception", err);
        } catch (HeuristicRollbackException err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Rollback Exception", err);
        } // ... really now Oracle?
        catch (Exception err) {
            Logger.getLogger(AdminService.class.getName()).log(Level.SEVERE, "Misc. Exception", err);
        }
    }
}
