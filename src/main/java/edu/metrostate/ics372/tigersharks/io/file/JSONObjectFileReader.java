package edu.metrostate.ics372.tigersharks.io.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.support.TigersharkException;
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

    /*  */
    private static final String ROOT_ELEMENT = "library_items";
    private static final String KEY_TYPE = "item_type";
    private static final String KEY_ID = "item_id";
    private static final String KEY_NAME = "item_name";
    private static final String KEY_ARTIST = "item_artist";
    private static final String KEY_AUTHOR = "item_author";
    private static final String KEY_VOLUME = "item_volume";

    /**
     *
     */
    private final int libraryId;

    /**
     * Read contents of a file into data List.
     *
     * Parse the file contents. Retrieve "library_items" key as List<JSONObject>. Add each element of List to data.
     *
     * @param inputStream initialized with path to json file.
     */
    public JSONObjectFileReader(InputStream inputStream, int libraryId) throws TigersharkException {
        super(inputStream);
        this.libraryId = libraryId;
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws TigersharkException
     */
    @Override
    protected List<JSONObject> getData(InputStream inputStream) throws TigersharkException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(inputStreamReader);
            if (jsonObject == null) {
                throw new TigersharkException("unable to parse file", new ParseException(0));
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get(ROOT_ELEMENT); // parse file as json get library items as a list and add each element to data
            if (jsonArray == null || jsonArray.size() == 0) {
                throw new TigersharkException("no item data found", new ParseException(1));
            }
            return jsonArray;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    protected Function<JSONObject, Optional<LibraryItem>> getMap() {
        return jsonObject -> {
            if(!jsonObject.containsKey(KEY_ID) || !jsonObject.containsKey(KEY_NAME) || !jsonObject.containsKey(KEY_TYPE)) { // is there an item type field?
                return Optional.empty();
            }

            final String id = jsonObject.get(KEY_ID).toString().trim();
            if (id.equals("")){
                return Optional.empty();
            }

            final String name = jsonObject.get(KEY_NAME).toString().trim();
            if (name.equals("")){
                return Optional.empty();
            }

            final LibraryItem.Type type;
            try {
                type = LibraryItem.Type.valueOf(jsonObject.get(KEY_TYPE).toString().trim().toUpperCase());
            } catch (Exception e) {
                return Optional.empty();
            }

            final String metadata;
            if(type.compareTo(LibraryItem.Type.CD) == 0) {
                if(!jsonObject.containsKey(KEY_ARTIST)) {
                    return Optional.empty();
                }
                metadata = jsonObject.get(KEY_ARTIST).toString().trim();
                if (metadata.equals("")){
                    return Optional.empty();
                }
            } else if(type.compareTo(LibraryItem.Type.BOOK) == 0) {
                if(!jsonObject.containsKey(KEY_AUTHOR)) {
                    return Optional.empty();
                }
                metadata = jsonObject.get(KEY_AUTHOR).toString().trim();
                if (metadata.equals("")){
                    return Optional.empty();
                }
            } else if(type.compareTo(LibraryItem.Type.MAGAZINE) == 0 && jsonObject.containsKey(KEY_VOLUME)) {
                metadata = jsonObject.get(KEY_VOLUME).toString().trim();
                if (metadata.equals("")){
                    return Optional.empty();
                }
            } else {
                metadata = null;
            }

            return Optional.of(new LibraryItem(id, name, type, metadata, libraryId, null, null));
        };
    }
}
