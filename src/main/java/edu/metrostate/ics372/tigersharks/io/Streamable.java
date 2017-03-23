package edu.metrostate.ics372.tigersharks.io;

import edu.metrostate.ics372.tigersharks.support.TigersharkException;

import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */

@FunctionalInterface
public interface Streamable<T> {
    Stream<T> stream() throws TigersharkException;
}
