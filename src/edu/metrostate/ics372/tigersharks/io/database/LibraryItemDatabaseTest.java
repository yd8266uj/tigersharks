package edu.metrostate.ics372.tigersharks.io.database;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sleig on 3/21/2017.
 */
public class LibraryItemDatabaseTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void getInstance() throws Exception {
        Assert.assertEquals(LibraryItemDatabase.getInstance(),LibraryItemDatabase.getInstance());
        Assert.assertTrue(LibraryItemDatabase.getInstance() != null);
    }

    @org.junit.Test
    public void selectAll() throws Exception {
        LibraryItemDatabase libraryItemDatabase = LibraryItemDatabase.getInstance();
        List<LibraryItem> libraryItemList = libraryItemDatabase.selectAll();
    }

    @org.junit.Test
    public void update() throws Exception {

    }
}