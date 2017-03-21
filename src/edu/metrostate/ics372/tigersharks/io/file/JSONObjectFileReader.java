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
public class JSONObjectFileReader extends FileReader<JSONObject, LibraryItem> {
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
        return jsonObject -> {
            if(!jsonObject.containsKey("item_type")) { // is there an item type field?
                return null; // could not create Loanable
            }
            final String id = jsonObject.get("item_id").toString();
            final String name = jsonObject.get("item_name").toString();
            final LibraryItem.Type type = LibraryItem.Type.valueOf(jsonObject.get("item_type").toString());
            final String metadata;
            if(jsonObject.containsKey("item_artist")) {
                metadata = jsonObject.get("item_artist").toString();
            } else if(!jsonObject.containsKey("item_author")) {
                metadata = jsonObject.get("item_author").toString();
            } else {
                metadata = null;
            }
            return new LibraryItem(id, name, type, metadata);
        };
    }
}
