package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.watertemplate.Template;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Created by sleig on 3/16/2017.
 */
public class Item extends Template {

    private final String title = "library";

    public Item(LibraryItem libraryItem) {
        //this.title = libraryItem.getName();
        addMappedObject("item", libraryItem, (item) -> {
            item.add("type", libraryItem.getType());
            item.add("name", libraryItem.getName());
            item.add("id", libraryItem.getId());
            Optional<LocalDate> dueDateOptional = libraryItem.getDueDate();
            if (dueDateOptional.isPresent()) {
                item.add("dueDate", dueDateOptional.get().format(DateTimeFormatter.ISO_LOCAL_DATE));
                item.add("hasDueDate", true);
            } else {
                item.add("dueDate", "");
                item.add("hasDueDate", false);
            }
            Optional<String> metadataOptional = libraryItem.getMetadata();
            if (metadataOptional.isPresent()) {
                item.add("metadata", metadataOptional.get());
                item.add("hasMetadata", true);
            } else {
                item.add("metadata", "");
                item.add("hasMetadata", false);
            }
            Optional<Integer> libraryIdOptional = libraryItem.getLibraryId();
            if (libraryIdOptional.isPresent()) {
                item.add("libraryId", String.valueOf(libraryIdOptional.get()));
                item.add("hasLibraryId", true);
            } else {
                item.add("libraryId", "");
                item.add("hasLibraryId", false);
            }
            Optional<String> patronIdOptional = libraryItem.getPatronId();
            if (patronIdOptional.isPresent()) {
                item.add("patronId", patronIdOptional.get());
                item.add("hasPatronId", true);
            } else {
                item.add("patronId", "");
                item.add("hasPatronId", false);
            }
        });
    }

    @Override
    protected Template getMasterTemplate() {
        return new MasterTemplate(title);
    }

    @Override
    protected String getFilePath() {
        return "item.html";
    }
}
