package edu.metrostate.ics372.tigersharks.io.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.metrostate.ics372.tigersharks.LibraryItem;
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
public class JSONObjectFileReader extends edu.metrostate.ics372.tigersharks.io.file.FileReader<JSONObject, LibraryItem> {
    private static final String ROOT_ELEMENT = "library_items";

    /**
     * Read contents of a file into data List.
     *
     * Parse the file contents. Retrieve "library_items" key as List<JSONObject>. Add each element of List to data.
     *
     * @param inputStream initialized with path to json file.
     */
    public JSONObjectFileReader(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    protected List<JSONObject> getData(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(inputStreamReader);
            return (JSONArray) jsonObject.get(ROOT_ELEMENT); // parse file as json get library items as a list and add each element to data
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected Function<JSONObject, LibraryItem> getMap() {
        return o -> {
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
            return null;};
    }
}
