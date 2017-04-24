package edu.metrostate.ics372.tigersharks.www;

import com.google.gson.Gson;
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

import javax.servlet.MultipartConfigElement;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static spark.Spark.*;

/**
 * controller responsible for processing and responding to http requests
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class WebService {

    /* constants */

    private static final String PATH_LIBRARY = "library";
    private static final String PATH_HOME = "home";
    private static final String PATH_UPLOAD = "upload";
    private static final String PATH_ITEM = "item";

    private static final String PARAM_LIBRARYID = ":libraryId";
    private static final String PARAM_ITEMID = ":itemId";

    private static final String ENDPOINT_HOME = "/" + PATH_HOME;
    private static final String ENDPOINT_ITEM = "/" + PATH_LIBRARY + "/" + PARAM_LIBRARYID + "/" + PATH_ITEM + "/" + PARAM_ITEMID;
    private static final String ENDPOINT_ITEMS = "/" + PATH_LIBRARY + "/" + PARAM_LIBRARYID;
    private static final String ENDPOINT_UPLOAD = "/" + PATH_LIBRARY + "/" + PARAM_LIBRARYID + "/" + PATH_UPLOAD;

    private static final String QUERYPARAM_RESPONSE_KEY = "r";
    private static final String QUERYPARAM_RESPONSE_HTML = "html";
    private static final String QUERYPARAM_RESPONSE_JSON = "json";
    private static final String QUERYPARAM_TELEPHONE_KEY = "telephone";
    private static final String QUERYPARAM_BUTTON_KEY = "button";
    private static final String QUERYPARAM_BUTTON_OUT = "out";
    private static final String QUERYPARAM_BUTTON_IN = "in";

    private static final String RESPONSE_TYPE_JSON = "application/json";
    private static final String RESPONSE_TYPE_HTML = "text/html";

    /**
     * supplies items to be filters or altered o
     */
    private final Streamable<LibraryItem> libraryItemStreamable;

    /**
     * accepts Library items with side effects
     */
    private final Consumer<LibraryItem> libraryItemConsumer;

    /**
     * Constructor. initialize with a source of data and somewhere to put it
     *
     * @param libraryItemStreamable a source of data
     * @param libraryItemConsumer somewhere to put it
     */
    public WebService(Streamable<LibraryItem> libraryItemStreamable, Consumer<LibraryItem> libraryItemConsumer) {
        this.libraryItemStreamable = libraryItemStreamable;
        this.libraryItemConsumer = libraryItemConsumer;
    }

    /**
     * start the webserver. listen for requests and respond appropriately
     */
    public void start() {

        /* show homepage with list of libraries */
        get(ENDPOINT_HOME, (request, response) -> {
            if(request.queryParams(QUERYPARAM_RESPONSE_KEY) != null) {
                switch (request.queryParams(QUERYPARAM_RESPONSE_KEY)) {
                    case QUERYPARAM_RESPONSE_JSON:
                        response.type(RESPONSE_TYPE_JSON);
                        return null;
                    case QUERYPARAM_RESPONSE_HTML:
                    default:
                }
            }
            return new Home().render();
        });

        /* show details of an item */
        get(ENDPOINT_ITEM, (request, response) -> {

            /* get parameters from url */
            final String itemId = request.params(PARAM_ITEMID); // current item id
            final String libraryId = request.params(PARAM_LIBRARYID); // current library id

            /**
             * a potential match from the database
             */
            Optional<LibraryItem> libraryItemOptional = Servicable.read(libraryItemStreamable, libraryItem -> // read the database
                    libraryItem.getId().equals(itemId) && LibraryItem.isInLibrary(Integer.valueOf(libraryId)).test(libraryItem)); // look for current library and item id

            /* if we found one process it */
             // if there is an item
            if(libraryItemOptional.isPresent()) {
                if (request.queryParams(QUERYPARAM_RESPONSE_KEY) != null) {
                    switch (request.queryParams(QUERYPARAM_RESPONSE_KEY)) {
                        case QUERYPARAM_RESPONSE_JSON:
                            response.type(RESPONSE_TYPE_JSON);
                            Gson gson = new Gson();
                            return gson.toJson(libraryItemOptional.get());
                        case QUERYPARAM_RESPONSE_HTML:
                        default:
                            response.type(RESPONSE_TYPE_HTML);
                    }
                }
                return new Item(libraryItemOptional.get()).render(); // generate a webpage with details and display
            }
            return null; // not a valid request
        });

        /* process request to check an item in or out */
        post(ENDPOINT_ITEM, (request, response) -> {

            /* get parameters from url */
            final String itemId = request.params(PARAM_ITEMID); // current item id
            final String libraryId = request.params(PARAM_LIBRARYID); // current library id


            /* get fields from submitted form */
            final String buttonValue = request.queryParams(QUERYPARAM_BUTTON_KEY); // was checkin or check out button pressed
            final String state = request.queryParams("s"); // we could do something with this if we needed to
            final String patronId; // the requesting library patron
            if (buttonValue.equals(QUERYPARAM_BUTTON_OUT)) { // if they are checking out
                patronId = request.queryParams(QUERYPARAM_TELEPHONE_KEY); // set the patron id
            } else { // otherwise
                patronId = null;  // we dont care. also, there isnt one
            }

            /**
             * potentially a matching item from the database
             */
            Optional<LibraryItem> libraryItemOptional = Servicable.read(libraryItemStreamable, libraryItem -> // read the database
                    libraryItem.getId().equals(itemId) && LibraryItem.isInLibrary(Integer.valueOf(libraryId)).test(libraryItem)); // look at current library and item id

            response.redirect(request.uri()); // let browser know it should send a get request to the current page after this

            /* process library item */
            if (libraryItemOptional.isPresent()) { // if there was a match

                LibraryItem libraryItem = libraryItemOptional.get(); // get it

                /* determine the type of request */
                if (buttonValue.equals(QUERYPARAM_BUTTON_OUT)) { // if checking out

                    if (!libraryItem.getDueDate().isPresent()) { // and it is not already checked out

                        libraryItem.checkout(patronId); // check out the current item
                        Servicable.update(libraryItemConsumer, libraryItem); // and update the database

                        return null;
                    }
                } else if (buttonValue.equals(QUERYPARAM_BUTTON_IN)) { // if this was a checkin request

                    libraryItem.checkin(); // check the item in
                    Servicable.update(libraryItemConsumer, libraryItem); // update the database
                }
            }

            return null; // not a valid request
        });

        /* show all items at a library */
        get(ENDPOINT_ITEMS, (request, response) -> {
            List<LibraryItem> libraryItemList = Servicable.readAll(libraryItemStreamable).stream() // get all the items
                    .filter(LibraryItem.isInLibrary(Integer.valueOf(request.params(PARAM_LIBRARYID)))) // filter by library
                    //.sorted(LibraryItem.sortByType.reversed()) // sort by item type
                    .sorted(LibraryItem.sortByName) // sort by item name
                    .collect(Collectors.toList());

            if (request.queryParams(QUERYPARAM_RESPONSE_KEY) != null) {
                switch (request.queryParams(QUERYPARAM_RESPONSE_KEY)) {
                    case QUERYPARAM_RESPONSE_JSON:
                        response.type(RESPONSE_TYPE_JSON);
                        Gson gson = new Gson();
                        return libraryItemList.stream()
                                .map(gson::toJson)
                                .collect(Collectors.joining(",","[","]"));
                    case QUERYPARAM_RESPONSE_HTML:
                        response.type(RESPONSE_TYPE_HTML);
                    default:
                }
            }
            return new Items(libraryItemList, request.params(PARAM_LIBRARYID)).render(); // tell the page what library this is and display items
        });

        /* show upload form for a library */
        get(ENDPOINT_UPLOAD, (request, response) -> {

            /* get parameters from url */
            final String libraryId = request.params(PARAM_LIBRARYID); // the current library

            if(request.queryParams(QUERYPARAM_RESPONSE_KEY) != null) {
                switch (request.queryParams(QUERYPARAM_RESPONSE_KEY)) {
                    case QUERYPARAM_RESPONSE_JSON:
                        response.type(RESPONSE_TYPE_JSON);
                        return null;
                    case QUERYPARAM_RESPONSE_HTML:
                    default:
                }
            }

            return new Upload(libraryId).render(); // display a new upload page
        });

        /* process file upload */
        post(ENDPOINT_UPLOAD, (request, response) -> {
            final String libraryId = request.params(PARAM_LIBRARYID);
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
            response.redirect("/" + PATH_LIBRARY + "/" + libraryId); //send back to current library browse
            return null;
        });

        /* page not found 404 */
        notFound((req, res) -> {
            res.redirect(ENDPOINT_HOME); // send it home
            return "page not found";
        });

        /* server error 500 */
        internalServerError((req, res) -> {
            res.redirect(ENDPOINT_HOME); // send it home
            return "oops something went wrong!";
        });
    }
}

