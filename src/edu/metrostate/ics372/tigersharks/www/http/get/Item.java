package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.Loanable;
import org.watertemplate.Template;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by sleig on 3/16/2017.
 */
public class Item extends Template {

    public Item(LibraryItem libraryItem) {
        addMappedObject("item", libraryItem, (item) -> {
            item.add("meta", libraryItem.getMetadata());
            item.add("id", libraryItem.getId());
            item.add("name", libraryItem.getName());
            item.add("type", libraryItem.getType());
        });
    }

    @Override
    protected String getFilePath() {
        return "item.html";
    }
}
