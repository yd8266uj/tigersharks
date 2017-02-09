package edu.metrostate.ics372.tigersharks;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for storing Loanables and checkin/out of Loanable by identifier.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class Library {

    /**
     * Store of loanable items.
     */
    private final HashMap<String, Loanable> catalog;

    /**
     * Build catalog of items
     *
     * @param supplier source of Loanable items
     */
    Library(Supplier<Loanable> supplier) {
        catalog = Stream.generate(supplier) // get the next Loanable
                .limit(((FileReader) supplier).size()) // stop reading after supplier.size() elements are read
                .collect(Collectors.toMap( // put each item into a hash map
                    Loanable::getItemId,
                    Function.identity(),
                    (l1,l2) -> null,
                    HashMap<String, Loanable>::new));
    }

    /**
     * Checkout item from catalog
     *
     * @param itemId item identifier
     * @return due date
     */
    LocalDate checkout(String itemId) {
        if(catalog.containsKey(itemId)) { // does catalog have an item with this id?
            return catalog.get(itemId).checkout(); // try to checkout item
        }
        return null;
    }

    /**
     * Checkin item to catalog
     *
     * @param itemId item identifier
     * @return checkin result status
     */
    boolean checkin(String itemId) {
        if(catalog.containsKey(itemId)) { // does catalog have an item with this id?
            return catalog.get(itemId).checkin(); // try to checkin item
        }
        return false;
    }

    /**
     * Parse command line arguments and drive project
     *
     * @param args command line arguments
     */
    public static void main(String args[]) {
        try {
            Library library = new Library(new FileReader(new java.io.FileReader(args[0]))); //initialize a new library with a file path passed into the command line
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.next(); // grab first word input
                switch (input.toLowerCase()) { // normalize input
                    case "checkin":
                        input = scanner.next(); // grab second word input
                        if (library.checkin(input)) { // could we checkin this item?
                            System.out.println("Item " + input + " checked in"); // print checkin success
                        } else {
                            System.out.println("Item " + input + " could not be checked in"); // print checkin failure
                        }
                        break;
                    case "checkout":
                        input = scanner.next(); // grab second word input
                        LocalDate dueDate = library.checkout(input); // try to checkout item
                        if (dueDate != null) { // is there a due date?
                            System.out.println("Item " + input + " is due on " + dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)); // print due date
                        } else {
                            System.out.println("Item " + input + " could not be checked out"); // could not print due date
                        }
                        break;
                    case "exit":
                        return; // stop listening for input and exit program
                    default:
                        System.out.println("Usage:"); // unknown command print help
                        System.out.println("\tcheckin <itemid>");
                        System.out.println("\tcheckout <itemid>");
                        System.out.println("\texit");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
