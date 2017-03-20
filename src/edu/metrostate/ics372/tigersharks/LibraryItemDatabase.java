package edu.metrostate.ics372.tigersharks;

import java.sql.*;
import java.util.List;

/**
 * Created by sleig on 3/19/2017.
 */
public class LibraryItemDatabase implements Database<LibraryItem> {
    private Connection connection;
    private static class SingletonHolder {
        static LibraryItemDatabase instance = new LibraryItemDatabase();
    }

    private LibraryItemDatabase() {
        try {
            this.connection = DriverManager.getConnection("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LibraryItemDatabase getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public List<LibraryItem> selectAll() {
        return null;
    }

    @Override
    public void update(LibraryItem libraryItem) {

    }
}
