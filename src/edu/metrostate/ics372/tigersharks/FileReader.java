package edu.metrostate.ics372.tigersharks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Responsible for generating a supply of Loanable items from a JSON file.
 */
public class FileReader implements Supplier<Loanable> {
    /**
     * A list of items read from the file reader.
     */
    private final LinkedList<JSONObject> data = new LinkedList<>();

    /**
     * Read contents of a file into data List.
     *
     * Parse the file contents. Retrieve "library_items" key as List<JSONObject>. Add each element of List to data.
     *
     * @param file initialized with path to json file.
     */
    public FileReader(java.io.FileReader file) {
        try {
            data.addAll((JSONArray)((JSONObject) new JSONParser().parse( file )).get("library_items"));
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provide the first Loanable
     *
     * Retrieve the first item of data and use it to create a new loanable.
     *
     * @return first Loanable item
     */
    @Override
    public Loanable get() {
        return new Map().apply(data.pollFirst());
    }

    /**
     * provide the length of data
     *
     * @return the length of data
     */
    public int size() {
        return data.size();
    }

    /**
     * Responsible for converting parsed JSONObjects to a Loanable.
     */
    private static class Map implements Function<JSONObject, Loanable> {
        /**
         * A mapping function. converts a JSONObject to a Loanable.
         *
         * @param o JSONOBject as HashMAp with data to initialize new Loanable.
         * @return Loanable instance of LibraryItem.
         */
        @Override
        public Loanable apply(JSONObject o) {
            if(!o.containsKey("item_type")) return null;
            switch (o.get("item_type").toString().toLowerCase()) {
                case "cd":
                    return LibraryItem.makeLibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.CD, o.get("item_artist").toString());
                case "dvd":
                    return LibraryItem.makeLibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.DVD);
                case "magazine":
                    return LibraryItem.makeLibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.MAGAZINE);
                case "book":
                    return LibraryItem.makeLibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.BOOK, o.get("item_author").toString());
            }
            return null;
        }
    }
}
