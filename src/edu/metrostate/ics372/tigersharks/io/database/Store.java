package edu.metrostate.ics372.tigersharks.io.database;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.io.Streamable;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */
public class Store implements Consumer<LibraryItem>, Streamable<LibraryItem> {
    private Database<LibraryItem> libraryItemDatabase;

    public Store(Database<LibraryItem> libraryItemDatabase) {
        this.libraryItemDatabase = libraryItemDatabase;
    }

    @Override
    public void accept(LibraryItem libraryItem) {
        libraryItemDatabase.update(libraryItem);
    }

    public Stream<LibraryItem> stream() {
        return libraryItemDatabase.selectAll().stream();
    }
}
