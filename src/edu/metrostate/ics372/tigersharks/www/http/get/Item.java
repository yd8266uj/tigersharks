package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.Loanable;
import org.watertemplate.Template;

import java.time.format.DateTimeFormatter;

/**
 * Created by sleig on 3/16/2017.
 */
public class Item extends Template {

    public Item(LibraryItem libraryItem) {
        add("meta",libraryItem.getMetadata());
    }

    @Override
    protected String getFilePath() {
        return "item.html";
    }
}
