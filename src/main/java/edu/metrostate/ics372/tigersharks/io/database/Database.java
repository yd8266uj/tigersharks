package edu.metrostate.ics372.tigersharks.io.database;

import java.util.List;

/**
 * this object can access a list of items and store items in a list? or maybe it just makes them up
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public interface Database<T> {
    List<T> selectAll();
    void update(T t);
}
