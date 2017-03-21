package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.watertemplate.Template;

import java.util.Optional;

/**
 * Created by sleig on 3/16/2017.
 */
public class Item extends Template {

    public Item(LibraryItem libraryItem) {
        addMappedObject("item", libraryItem, (item) -> {

            Optional<String> metadata = libraryItem.getMetadata();
            if (metadata.isPresent()) {
                item.add("meta", metadata.get());
            } else {
                item.add("meta", "");
            }
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
