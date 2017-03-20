package edu.metrostate.ics372.tigersharks;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */
public class Store implements Consumer<LibraryItem>, Streamable<LibraryItem> {
    private Database<LibraryItem> libraryItemDatabase;

    private Store(Database<LibraryItem> libraryItemDatabase) {
        this.libraryItemDatabase = libraryItemDatabase;
    }

    @Override
    public void accept(LibraryItem libraryItem) {
        libraryItemDatabase.update(libraryItem);
        //data.add(libraryItem);
    }

    public Stream<LibraryItem> stream() {
        return libraryItemDatabase.selectAll().stream();
        /*
        data = new ArrayList<>();
        data.add(new LibraryItem("a","b", LibraryItem.Type.CD,"e"));
        data.add(new LibraryItem("a","c", LibraryItem.Type.CD,"f"));
        data.add(new LibraryItem("a","d", LibraryItem.Type.CD,"g"));
        return data.stream();
        */
    }
}
