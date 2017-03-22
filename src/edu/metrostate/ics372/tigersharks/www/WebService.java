package edu.metrostate.ics372.tigersharks.www;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.Servicable;
import edu.metrostate.ics372.tigersharks.io.Streamable;
import edu.metrostate.ics372.tigersharks.io.database.LibraryItemDatabase;
import edu.metrostate.ics372.tigersharks.io.Store;
import edu.metrostate.ics372.tigersharks.io.file.JSONObjectFileReader;
import edu.metrostate.ics372.tigersharks.io.file.XMLElementFileReader;
import edu.metrostate.ics372.tigersharks.www.http.get.Home;
import edu.metrostate.ics372.tigersharks.www.http.get.Item;
import edu.metrostate.ics372.tigersharks.www.http.get.Items;
import edu.metrostate.ics372.tigersharks.www.http.get.Upload;
import spark.Spark;

import javax.servlet.MultipartConfigElement;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static spark.Spark.*;

/**
 * Created by sleig on 3/15/2017.
 */
public class WebService {

    private static final String ENDPOINT_HOME = "/home";
    private static final String ENDPOINT_ITEM = "/library/:libraryId/item/:itemId";
    private static final String ENDPOINT_ITEMS = "/library/:libraryId";
    private static final String ENDPOINT_UPLOAD = "/library/:libraryId//upload";

    private final Streamable<LibraryItem> libraryItemStreamable;
    private final Consumer<LibraryItem> libraryItemConsumer;

    private WebService(Streamable<LibraryItem> libraryItemStreamable, Consumer<LibraryItem> libraryItemConsumer) {
        this.libraryItemStreamable = libraryItemStreamable;
        this.libraryItemConsumer = libraryItemConsumer;
    }

    public void start() {

        get(ENDPOINT_HOME, (request, response) -> new Home().render());

        get(ENDPOINT_ITEM, (request, response) -> {
            final String itemId = request.params(":itemId");
            final String libraryId = request.params(":libraryId");
            Optional<LibraryItem> libraryItemOptional = Servicable.read(libraryItemStreamable, libraryItem ->
                    libraryItem.getId().equals(itemId) && LibraryItem.isInLibrary(Integer.valueOf(libraryId)).test(libraryItem));
            if(libraryItemOptional.isPresent()) {
                return new Item(libraryItemOptional.get()).render();
            }
            return null;
        });

        post(ENDPOINT_ITEM, (request, response) -> {
            final String itemId = request.params(":itemId");
            final String libraryId = request.params(":libraryId");
            final String buttonValue = request.queryParams("button");
            final String patronId;
            if (buttonValue.equals("out")) {
                patronId = request.queryParams("telephone");
            } else {
                patronId = "";
            }
            Optional<LibraryItem> libraryItemOptional = Servicable.read(libraryItemStreamable, libraryItem ->
                    libraryItem.getId().equals(itemId) && LibraryItem.isInLibrary(Integer.valueOf(libraryId)).test(libraryItem));
            response.redirect(request.uri());
            if (libraryItemOptional.isPresent()) {
                LibraryItem libraryItem = libraryItemOptional.get();
                if (buttonValue.equals("out")) {
                    if (!libraryItem.getDueDate().isPresent()) {
                        libraryItem.checkout(patronId);
                        libraryItemConsumer.accept(libraryItem);
                        return "<a href=\"" + libraryId + "\">" + libraryItem.getName() + "</a> was checked out from library " + libraryId + " by " + patronId;
                    }
                } else if (buttonValue.equals("in")) {
                    libraryItem.checkin();
                    libraryItemConsumer.accept(libraryItem);
                }
            }
            return null;
        });

        get(ENDPOINT_ITEMS, (request, response) -> new Items(Servicable.readAll(libraryItemStreamable).stream()
                .filter(LibraryItem.isInLibrary(Integer.valueOf(request.params(":libraryId"))))
                //.sorted(LibraryItem.sortByType.reversed())
                .sorted(LibraryItem.sortByName)
                .collect(Collectors.toList())
        ).render());

        get(ENDPOINT_UPLOAD, (request, response) -> {
            final String libraryId = request.params(":libraryId");
            return new Upload(libraryId).render();
        });

        post(ENDPOINT_UPLOAD, (request, response) -> {
            final String libraryId = request.params(":libraryId");
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream inputStream = request.raw().getPart("file").getInputStream()) {
                final String fileName = request.raw()
                        .getPart("file")
                        .getSubmittedFileName();
                if(fileName.endsWith(".xml")) {
                    new XMLElementFileReader(inputStream, Integer.valueOf(libraryId))
                            .stream()
                            .forEach(libraryItemConsumer);
                } else if(fileName.endsWith(".json")) {
                    new JSONObjectFileReader(inputStream, Integer.valueOf(libraryId))
                            .stream()
                            .forEach(libraryItemConsumer);
                }
            }
            response.redirect(request.uri());
            return "File uploaded";
        });
    }

    public static void main(String[] args) {
        Store<LibraryItem> libraryItemStore = new Store<>(LibraryItemDatabase.getInstance());
        new WebService(libraryItemStore,libraryItemStore).start();
    }
}

