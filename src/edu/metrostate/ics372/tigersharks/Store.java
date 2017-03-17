package edu.metrostate.ics372.tigersharks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/16/2017.
 */
public class Store implements Consumer<LibraryItem>, Streamable<LibraryItem> {
    private List<LibraryItem> data;

    private static class SingletonHolder {
        static Store instance = new Store();
    }

    private Store() {
        data = new ArrayList<>();
        data.add(new LibraryItem("a","b", LibraryItem.Type.CD,"e"));
        data.add(new LibraryItem("a","c", LibraryItem.Type.CD,"f"));
        data.add(new LibraryItem("a","d", LibraryItem.Type.CD,"g"));
        //load data from database
    }

    public static Store getInstance() {
        return SingletonHolder.instance;
    }

    public int size() {
        return data.size();
    }

    @Override
    public void accept(LibraryItem loanable) {
        //check if exists
        //add to database
        //update data
        data.add(loanable);
    }

    public Stream<LibraryItem> stream() {
        return data.stream();
    }
}
