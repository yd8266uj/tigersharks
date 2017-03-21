package edu.metrostate.ics372.tigersharks;

import edu.metrostate.ics372.tigersharks.io.Store;
import edu.metrostate.ics372.tigersharks.io.Streamable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Responsible for storing Loanables and checkin/out of Loanable by identifier.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
/*
public interface Library<T> extends Servicable<T> {

    static void createAll(Consumer<T> tConsumer, List<LibraryItem> loanables) {
        loanables.stream().forEach(store);
    }

    @Override
    public void create(Consumer<T> tConsumer, LibraryItem loanable) {
        store.accept(loanable);
    }

    @Override
    public List<LibraryItem> readAll() {
        return store.stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LibraryItem> read(Predicate<LibraryItem> loanablePredicate) {
        return readAll().stream()
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
}
*/