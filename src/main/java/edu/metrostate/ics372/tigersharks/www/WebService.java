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

import javax.servlet.MultipartConfigElement;

import java.io.InputStream;
import java.util.Optional;
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

    /* path constants */
    private static final String ENDPOINT_HOME = "/home";
    private static final String ENDPOINT_ITEM = "/library/:libraryId/item/:itemId";
    private static final String ENDPOINT_ITEMS = "/library/:libraryId";
    private static final String ENDPOINT_UPLOAD = "/library/:libraryId//upload";

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
        get(ENDPOINT_HOME, (request, response) -> new Home().render());

        /* show details of an item */
        get(ENDPOINT_ITEM, (request, response) -> {

            /* get parameters from url */
            final String itemId = request.params(":itemId"); // current item id
            final String libraryId = request.params(":libraryId"); // curent library id

            /**
             * a potential match from the database
             */
            Optional<LibraryItem> libraryItemOptional = Servicable.read(libraryItemStreamable, libraryItem -> // read the database
                    libraryItem.getId().equals(itemId) && LibraryItem.isInLibrary(Integer.valueOf(libraryId)).test(libraryItem)); // look for current library and item id

            /* if we found one process it */
            if(libraryItemOptional.isPresent()) { // if there is an item
                return new Item(libraryItemOptional.get()).render(); // generate a webpage with details and display
            }

            return null; // not a valid request
        });

        /* process request to check an item in or out */
        post(ENDPOINT_ITEM, (request, response) -> {

            /* get paramters from url */
            final String itemId = request.params(":itemId"); // current item id
            final String libraryId = request.params(":libraryId"); // current library id

            /* get fields from submitted form */
            final String buttonValue = request.queryParams("button"); // was checkin or check out button pressed
            final String patronId; // the requesting library patron
            if (buttonValue.equals("out")) { // if they are checking out
                patronId = request.queryParams("telephone"); // set the patron id
            } else { // otherwise
                patronId = "";  // we dont care. also, there isnt one
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
                if (buttonValue.equals("out")) { // if checking out

                    if (!libraryItem.getDueDate().isPresent()) { // and it is not already checked out

                        libraryItem.checkout(patronId); // check out the current item
                        Servicable.update(libraryItemConsumer, libraryItem); // and update the databse

                        return "<a href=\"" + libraryId + "\">" + libraryItem.getName() + "</a> was checked out from library " + libraryId + " by " + patronId; // let stuborn browsers know what happened
                    }
                } else if (buttonValue.equals("in")) { // if this was a checkin request

                    libraryItem.checkin(); // check the item in
                    Servicable.update(libraryItemConsumer, libraryItem); // update the database
                }
            }

            return null; // not a valid request
        });

        /* show all items at a library */
        get(ENDPOINT_ITEMS, (request, response) -> new Items(Servicable.readAll(libraryItemStreamable).stream() // get all the items
                .filter(LibraryItem.isInLibrary(Integer.valueOf(request.params(":libraryId")))) // filter by library
                //.sorted(LibraryItem.sortByType.reversed()) // sort by item type
                .sorted(LibraryItem.sortByName) // sort by item name
                .collect(Collectors.toList()), // make into a list
                request.params(":libraryId")).render()); // tell the page what library this is and display items

        /* show upload form for a library */
        get(ENDPOINT_UPLOAD, (request, response) -> {

            /* get parameters from url */
            final String libraryId = request.params(":libraryId"); // the current library

            return new Upload(libraryId).render(); // display a new upload page
        });

        /* process file upload */
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
            response.redirect("/library/" + libraryId); //send back to current library browse
            return "File uploaded";
        });

        /* page not found 404 */
        notFound((req, res) -> {
            res.redirect("/home"); // send it home
            return "page not found";
        });

        /* server error 500 */
        internalServerError((req, res) -> {
            res.redirect("/home"); // send it home
            return "oops something went wrong!";
        });
    }
}

