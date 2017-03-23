package edu.metrostate.ics372.tigersharks.io;

import edu.metrostate.ics372.tigersharks.support.TigersharkException;

import java.util.stream.Stream;

/**
 * this object can be converted to a stream of objects
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */

@FunctionalInterface
public interface Streamable<T> {
    Stream<T> stream() throws TigersharkException;
}
