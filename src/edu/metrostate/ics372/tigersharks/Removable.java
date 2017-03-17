package edu.metrostate.ics372.tigersharks;

import java.util.function.Function;

/**
 * Created by sleig on 3/17/2017.
 */
public class Removable<T> {
    private final T t;

    private Removable(T t) {
        this.t = t;
    }

    public static <T> Removable<T> of(T t) {
        return new Removable<>(t);
    }

    public T get() {
        return t;
    }
}
