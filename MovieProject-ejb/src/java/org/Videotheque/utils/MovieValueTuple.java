/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.utils;

import org.Videotheque.Entities.Movie;

/**
 *
 * @author Phillip
 */
public class MovieValueTuple extends Tuple2<Movie,Integer> {
    public MovieValueTuple(Movie m, int i) {
        super(m,i);
    }
    
    @Override
    public int compareTo(Object o) {
        MovieValueTuple mvt = (MovieValueTuple) o;
        if(mvt.two() == two()) {
            return 0;
        }
        else if(two() < mvt.two()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
