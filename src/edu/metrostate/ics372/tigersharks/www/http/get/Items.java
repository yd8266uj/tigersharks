package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;

import org.watertemplate.Template;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Created by sleig on 3/16/2017.
 */
public class Items extends Template {

    public Items(List<LibraryItem> libraryItemList) {
        addCollection("items", libraryItemList, (item, items) -> {
            items.add("type", item.getType());
            items.add("name", item.getName());
            items.add("id", item.getId());
            Optional<LocalDate> dueDateOptional = item.getDueDate();
            if (dueDateOptional.isPresent()) {
                items.add("dueDate", dueDateOptional.get().format(DateTimeFormatter.BASIC_ISO_DATE));
                items.add("hasDueDate", true);
            } else {
                items.add("dueDate", "");
                items.add("hasDueDate", false);
            }
            Optional<String> metadataOptional = item.getMetadata();
            if (metadataOptional.isPresent()) {
                items.add("metadata", metadataOptional.get());
                items.add("hasMetadata", true);
            } else {
                items.add("metadata", "");
                items.add("hasMetadata", false);
            }
            Optional<Integer> libraryIdOptional = item.getLibraryId();
            if (libraryIdOptional.isPresent()) {
                items.add("libraryId", String.valueOf(libraryIdOptional.get()));
                items.add("hasLibraryId", true);
            } else {
                items.add("libraryId", "");
                items.add("hasLibraryId", false);
            }
            Optional<String> patronIdOptional = item.getPatronId();
            if (patronIdOptional.isPresent()) {
                items.add("patronId", patronIdOptional.get());
                items.add("hasPatronId", true);
            } else {
                items.add("patronId", "");
                items.add("hasPatronId", false);
            }
        });
    }

    @Override
    protected String getFilePath() {
        return "items.html";
    }
}
