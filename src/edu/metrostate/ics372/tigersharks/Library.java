package edu.metrostate.ics372.tigersharks;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Responsible for storing Loanables and checkin/out of Loanable by identifier.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class Library implements Servicable<LibraryItem> {

    @Override
    public void createAll(List<LibraryItem> loanables) {
        loanables.stream().forEach(Store.getInstance());
    }

    @Override
    public void create(LibraryItem loanable) {
        Store.getInstance().accept(loanable);
    }

    @Override
    public List<LibraryItem> readAll() {
        return Store.getInstance().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LibraryItem> read(Predicate<LibraryItem> loanablePredicate) {
        return Store.getInstance().stream()
                .filter(loanablePredicate)
                .findFirst();
    }

    @Override
    public void updateAll(List<LibraryItem> loanables) {
        createAll(loanables);
    }

    @Override
    public void update(LibraryItem loanable) {
        create(loanable);
    }

    @Override
    public void deleteAll(List<LibraryItem> loanables) {
        //loanables.stream().map(Removable::of).forEach(Store.getInstance());
    }

    @Override
    public void delete(LibraryItem loanable) {
        //create(loanable);
    }
}
