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
 * Created by sleig on 1/24/2017.
 */
public class Library {
    private final HashMap<String, Loanable> catalog;

    public Library(Supplier<Loanable> supplier) {
        catalog = Stream.generate(supplier)
                .limit(((FileReader) supplier).size())
                .collect(Collectors.toMap(
                    Loanable::getItemId,
                    Function.identity(),
                    (l1,l2) -> null,
                    HashMap<String, Loanable>::new));
    }

    public LocalDate checkout(String itemId) {
        if(catalog.containsKey(itemId)) {
            return catalog.get(itemId).checkout();
        }
        return null;
    }

    public boolean checkin(String itemId) {
        if(catalog.containsKey(itemId)) {
            return catalog.get(itemId).checkin();
        }
        return false;
    }

    public static void main(String args[]) {
        try {
            Library library = new Library(new FileReader(new java.io.FileReader(args[0])));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.next();
                switch (input.toLowerCase()) {
                    case "checkin":
                        input = scanner.next();
                        if (library.checkin(input)) {
                            System.out.println("Item " + input + " checked in");
                        } else {
                            System.out.println("Item " + input + " could not be checked in");
                        }
                        break;
                    case "checkout":
                        input = scanner.next();
                        LocalDate dueDate = library.checkout(input);
                        if (dueDate != null) {
                            System.out.println("Item " + input + " is due on " + dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                        } else {
                            System.out.println("Item " + input + " could not be checked out");
                        }
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Usage:");
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
