package edu.metrostate.ics372.tigersharks.io.database;

import edu.metrostate.ics372.tigersharks.LibraryItem;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by sleig on 3/19/2017.
 */
public class LibraryItemDatabase implements Database<LibraryItem> {
    private Connection connection;
    private boolean tableExists = false;

    private static final String databaseName = "library.db";
    private static final String databaseDirectory = "database";
    private static final String databaseDriver = "sqlite";
    private static final String tableName = "library";
    private static final String columnList = "id, name, type, metadata,libraryId, dueDate, patronId";

    private static class SingletonHolder {
        static LibraryItemDatabase instance = new LibraryItemDatabase();
    }

    private LibraryItemDatabase() {
        final String url = "jdbc:" + databaseDriver + ":" + databaseDirectory + "//" + databaseName;
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LibraryItemDatabase getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public List<LibraryItem> selectAll() {
        final String sql = "SELECT " + columnList + " FROM " + tableName;
        List<LibraryItem> libraryItemList = new ArrayList<>();
        try {
            if(!tableExists()) {
                createTable();
            }
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                final LocalDate dueDate;
                if(resultSet.getString("dueDate") == null) {
                    dueDate = null;
                } else {
                    dueDate = LocalDate.parse(resultSet.getString("dueDate"));
                }
                LibraryItem libraryItem = new LibraryItem(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        LibraryItem.Type.valueOf(resultSet.getString("type")),
                        resultSet.getString("metadata"),
                        resultSet.getInt("libraryId"),
                        dueDate,
                        resultSet.getInt("patronId"));
                libraryItemList.add(libraryItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libraryItemList;
    }

    @Override
    public void update(LibraryItem libraryItem) {
        final String id = libraryItem.getId();
        final String name = libraryItem.getName();
        final String type = libraryItem.getType();
        final Optional<String> metadataOptional = libraryItem.getMetadata();
        final Optional<Integer> libraryIdOptional = libraryItem.getLibraryId();
        final Optional<LocalDate> dueDateOptional = libraryItem.getDueDate();
        final Optional<Integer> patronIdOptional = libraryItem.getPatronId();

        final String sql = "INSERT OR REPLACE INTO " + tableName + "(" + columnList + ") VALUES (?,?,?,?,?,?,?);";

        try {
            if(!tableExists()) {
                createTable();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, type);
            if (metadataOptional.isPresent()) {
                preparedStatement.setString(4, metadataOptional.get());
            }
            if (libraryIdOptional.isPresent()) {
                preparedStatement.setInt(5, libraryIdOptional.get());
            } else {
                preparedStatement.setNull(5, Types.INTEGER);
            }
            if (dueDateOptional.isPresent()) {
                preparedStatement.setString(6, dueDateOptional.get().format(DateTimeFormatter.BASIC_ISO_DATE));
            }
            if (patronIdOptional.isPresent()) {
                preparedStatement.setInt(7, patronIdOptional.get());
            } else {
                preparedStatement.setNull(7, Types.INTEGER);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        final String sql = "CREATE TABLE " + tableName + " (" +
                "id    TEXT NOT NULL," +
                "name    TEXT NOT NULL," +
                "type    TEXT NOT NULL," +
                "metadata    TEXT," +
                "libraryId    INTEGER NOT NULL," +
                "dueDate    TEXT," +
                "patronId    INTEGER ," +
                "PRIMARY KEY (id, libraryId));";

        connection.createStatement().execute(sql);
    }

    private boolean tableExists() {
        if (tableExists) {
            return true;
        }
        try {
            tableExists = connection.getMetaData().getTables(null, null, tableName, null).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableExists;
    }

    public static void main(String[] args) {
        LibraryItemDatabase libraryItemDatabase = new LibraryItemDatabase();
        LibraryItem libraryItem = new LibraryItem("abc123", "The Similiaron", LibraryItem.Type.BOOK,"J.R,R Tolkien",1,null,0);
        libraryItem.checkout();
        libraryItemDatabase.update(libraryItem);
        List<LibraryItem> libraryItemList = libraryItemDatabase.selectAll();
        System.out.println(libraryItemList.size());
    }
}