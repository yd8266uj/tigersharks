package edu.metrostate.ics372.tigersharks;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by sleig on 3/16/2017.
 */
public interface Servicable<T> {
    void createAll(List<T> tList);
    void create(T t);
    List<T> readAll();
    Optional<LibraryItem> read(Predicate<T> tPredicate);
    void updateAll(List<T> tList);
    void update(T t);
    void deleteAll(List<T> tList);
    void delete(T t);
}
