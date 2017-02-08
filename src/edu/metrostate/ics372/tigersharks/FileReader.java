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
 * Created by sleig on 1/29/2017.
 */
public class FileReader implements Supplier<Loanable> {
    private final LinkedList<JSONObject> data = new LinkedList<>();

    public FileReader(java.io.FileReader file) {
        try {
            JSONArray libraryItems = (JSONArray)((JSONObject) new JSONParser().parse( file )).get("library_items");
            data.addAll(libraryItems);
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loanable get() {
        JSONObject j = data.pollFirst();
        return new Map().apply(j);
    }

    public int size() {
        return data.size();
    }

    private static class Map implements Function<JSONObject, Loanable> {
        @Override
        public Loanable apply(JSONObject o) {
            if(o==null) return null;
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
