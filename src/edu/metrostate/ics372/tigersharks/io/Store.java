package edu.metrostate.ics372.tigersharks.io;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.io.database.Database;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */
public class Store<T> implements Consumer<T>, Streamable<T> {
    private Database<T> database;

    public Store(Database<T> database) {
        this.database = database;
    }

    @Override
    public void accept(T libraryItem) {
        database.update(libraryItem);
    }

    public Stream<T> stream() {
        return database.selectAll().stream();
    }
}
