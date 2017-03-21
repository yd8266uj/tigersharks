package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.w3c.dom.Element;

import java.io.*;
import java.util.List;
import java.util.function.Function;

/**
 * Created by sleig on 3/21/2017.
 */
public class XMLElementFileReader extends FileReader<Element, LibraryItem> {
    public XMLElementFileReader(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected List<Element> getData(InputStream inputStream) {
        // use inputStream to get a list of Element and then return it
        return null;
    }

    @Override
    protected Function<Element, LibraryItem> getMap() {
        return element -> {
            //create new LibraryItem from element and retun it
            return null;
        };
    }
}
