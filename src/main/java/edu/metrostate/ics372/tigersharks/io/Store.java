package edu.metrostate.ics372.tigersharks.io;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.io.database.Database;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * combines the power of a consumer and a supplier all in one class
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
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
