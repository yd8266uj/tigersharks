package edu.metrostate.ics372.tigersharks;

import edu.metrostate.ics372.tigersharks.io.Store;
import edu.metrostate.ics372.tigersharks.io.database.LibraryItemDatabase;
import edu.metrostate.ics372.tigersharks.www.WebService;

/**
 * Created by sleig on 3/22/2017.
 */
public class Main {
    public static void main(String[] args) {
        Store<LibraryItem> libraryItemStore = new Store<>(LibraryItemDatabase.getInstance());
        new WebService(libraryItemStore,libraryItemStore).start();
    }
}
