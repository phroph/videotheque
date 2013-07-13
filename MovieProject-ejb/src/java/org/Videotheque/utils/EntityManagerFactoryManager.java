/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Seemingly redundant class which allows all Entities to share a single EMF
 * rather than each Entity creating it's own EMF. Improves deployment speed and
 * memory requirements.
 * @author Phillip
 */
public class EntityManagerFactoryManager {
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovieProject-ejbPU");
}
