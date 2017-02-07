package edu.metrostate.ics372.tigersharks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
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

        this.catalog = Stream.generate(supplier)
                .collect(Collectors.toMap(
                    Loanable::getItemId,
                    Function.identity(),
                    (l1,l2) -> null,
                    HashMap<String, Loanable>::new));
    }

    public LocalDate checkout(String itemId) {
        return catalog.get(itemId).checkout();
    }

    public boolean checkin(String itemId) {
        return catalog.get(itemId).checkin();
    }

    public static void main(String args[]) {
        Library library;
        try {
            library = new Library(new FileReader(new java.io.FileReader(new FileInputStream(args[0]).toString())));

            if(args[0].equalsIgnoreCase("checkin")){
                library.checkin(args[1]);
            }

            if(args[0].equalsIgnoreCase("checkout")){
                library.checkout(args[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
