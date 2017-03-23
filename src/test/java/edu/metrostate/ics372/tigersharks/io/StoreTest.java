package edu.metrostate.ics372.tigersharks.io;

import edu.metrostate.ics372.tigersharks.io.database.Database;
import edu.metrostate.ics372.tigersharks.io.database.LibraryItemDatabase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by sleig on 3/23/2017.
 */
public class StoreTest {
    @Test
    public void accept() throws Exception {
        Store<String> store = new Store<>(new Database<String>() {
            List<String> l = new ArrayList<>();
            @Override
            public List<String> selectAll() {
                return l;
            }

            @Override
            public void update(String string) {
                int i = l.indexOf(string);
                if(i >= 0) {
                    l.set(i,string);
                } else {
                    l.add(string);
                }
            }
        });

        List<String> input = new ArrayList<>();
        input.add("a");
        input.add("b");
        input.add("c");
        input.add("a");
        input.add("b");
        input.add("d");

        List<String> snapshot1 = store.stream().collect(Collectors.toList());
        assertEquals("not initially empty",snapshot1.size(),0);
        input.stream().forEach(store);
        List<String> snapshot2 = store.stream().collect(Collectors.toList());
        assertTrue("some inputs missing",snapshot2.containsAll(input));
        assertEquals("incorrect length after update",snapshot2.size(),4);
    }

    @Test
    public void stream() throws Exception {

    }

}