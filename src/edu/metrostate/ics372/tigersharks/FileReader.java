package edu.metrostate.ics372.tigersharks;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser; 
/**
 * Created by sleig on 1/29/2017.
 */

//Object type should be something more specific
public class FileReader implements Supplier<Loanable> {
    private final LinkedList<Object> data = new LinkedList<>();

    public FileReader(java.io.FileReader file) {
        JSONParser parser = new JSONParser(); 
            Object object = parser. 
                    parse(file); 
        JSONObject jsonObject = (JSONObject) object; 
        JSONArray LibraryItems = (JSONArray)jsonObject.get("library_items"); 
        data.add(LibraryItems); 
        //initialize data here
    }

    @Override
    public Loanable get() {
        return new Map().apply(data.pollFirst());
    }

    private static class Map implements Function<Object, Loanable> {
        @Override
        public Loanable apply(Object o) {
            return null;
        }
    }
}
