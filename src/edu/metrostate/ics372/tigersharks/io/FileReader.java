package edu.metrostate.ics372.tigersharks.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.Loanable;
import edu.metrostate.ics372.tigersharks.Streamable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Responsible for generating a supply of Loanable items from a JSON file.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class FileReader implements Streamable<Loanable> {

    /**
     * A list of items read from the file reader.
     */
    private final List<JSONObject> data = new ArrayList<>();

    /**
     * Read contents of a file into data List.
     *
     * Parse the file contents. Retrieve "library_items" key as List<JSONObject>. Add each element of List to data.
     *
     * @param file initialized with path to json file.
     */
    public FileReader(java.io.FileReader file) {
        try {
            data.addAll((JSONArray)((JSONObject) new JSONParser().parse( file )).get("library_items")); // parse file as json get library items as a list and add each element to data
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
    public Stream<Loanable> stream() {
        return data.stream().map(new Map()); // create a loanable from the next data item and return it
    }

    /**
     * provide the length of data
     *
     * @return the length of data
     */
    int size() {
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
            if(!o.containsKey("item_type")) { // is there an item type field?
                return null; // could not create Loanable
            }
            switch (o.get("item_type").toString().toLowerCase()) {
                case "cd":
                    //return new LibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.CD, o.get("item_artist").toString()); // return new LibraryItem.CD(...) from static factory
                case "dvd":
                    return new LibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.DVD); // return new LibraryItem.DVD(...) from static factory
                case "magazine":
                    return new LibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.MAGAZINE); // return new LibraryItem.Magazine(...) from static factory
                case "book":
                    //return new LibraryItem(o.get("item_name").toString(), o.get("item_id").toString(), LibraryItem.Type.BOOK, o.get("item_author").toString()); // return new LibraryItem.Book(...) from static factory
            }
            return null;
        }
    }
}
