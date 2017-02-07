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

//Object type should be something more specific
public class FileReader implements Supplier<Loanable> {
    private final LinkedList<JSONObject> data = new LinkedList<>();

    public FileReader(java.io.FileReader file) {
        try {
            data.addAll((JSONArray)((JSONObject) new JSONParser().parse(file)).get("library_items"));
        } catch (IOException|ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loanable get() {
        return new Map().apply(data.pollFirst());
    }

    public int size() {
        return data.size();
    }

    private static class Map implements Function<JSONObject, Loanable> {
        @Override
        public Loanable apply(JSONObject o) {
            return null;
        }
    }
}
