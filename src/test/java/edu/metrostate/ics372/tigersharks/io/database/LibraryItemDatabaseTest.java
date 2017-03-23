package edu.metrostate.ics372.tigersharks.io.database;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit testing a database is very hard...
 */
public class LibraryItemDatabaseTest {
    private testDB libraryItemDatabase;

    private static class testDB extends LibraryItemDatabase {
        public testDB() {
            super("test");
        }
    }

    @Before
    public void setup() throws Exception {
        this.libraryItemDatabase = new testDB();
    }

    @Test
    public void getInstance() throws Exception {
        assertEquals(LibraryItemDatabase.getInstance(),LibraryItemDatabase.getInstance());
    }

    @Test
    public void selectAll() throws Exception {
        List<LibraryItem> libraryItemList = new ArrayList<>();
        libraryItemList.add(new LibraryItem("a","b",LibraryItem.Type.BOOK,"c",0, LocalDate.now(),"d"));
        libraryItemList.add(new LibraryItem("c","d",LibraryItem.Type.BOOK,"c",0, LocalDate.now(),"d"));
        libraryItemList.add(new LibraryItem("e","f",LibraryItem.Type.BOOK,"c",0, LocalDate.now(),"e"));
        libraryItemList.add(new LibraryItem("g","h",LibraryItem.Type.BOOK,"c",0, LocalDate.now(),"d"));
        libraryItemList.forEach(libraryItemDatabase::update);
        assertEquals(libraryItemDatabase.selectAll().size(),4);
    }
}