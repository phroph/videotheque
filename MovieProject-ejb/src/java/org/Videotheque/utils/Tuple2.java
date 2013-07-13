/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.utils;

/**
 *
 * @author Phillip
 */
public class Tuple2<T1,T2> implements Comparable  {
    private T1 one;
    private T2 two;
    public Tuple2(T1 one, T2 two) {
        this.one = one;
        this.two = two;
    }
    public T1 one() {
        return one;
    }
    public T2 two() {
        return two;
    }
    public void one(T1 one) {
        this.one = one;
    }
    public void two(T2 two) {
        this.two = two;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
