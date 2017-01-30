package edu.metrostate.ics372.tigersharks;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by sleig on 1/29/2017.
 */

//Object type should be something more specific
public class FileReader implements Supplier<Loanable> {
    private final LinkedList<Object> data = new LinkedList<>();

    public FileReader(InputStream file) {
        //initialize data here
    }

    @Override
    public Loanable get() {
        return new Map().apply(data.pollFirst());
    }

    private static class Map implements Function<Object, Loanable> {
        @Override
        public Loanable apply(Object o) {
            return null;
        }
    }
}
