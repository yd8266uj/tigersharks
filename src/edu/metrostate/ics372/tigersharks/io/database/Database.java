package edu.metrostate.ics372.tigersharks.io.database;

import java.util.List;

/**
 * Created by sleig on 3/19/2017.
 */
public interface Database<T> {
    List<T> selectAll();
    void update(T t);
}
