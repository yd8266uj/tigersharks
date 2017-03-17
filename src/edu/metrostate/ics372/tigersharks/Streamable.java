package edu.metrostate.ics372.tigersharks;

import com.sun.istack.internal.NotNull;

import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */

@FunctionalInterface
public interface Streamable<T> {
    @NotNull
    Stream<T> stream();
}
