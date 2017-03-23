package edu.metrostate.ics372.tigersharks;

import edu.metrostate.ics372.tigersharks.io.Streamable;
import edu.metrostate.ics372.tigersharks.support.TigersharkException;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * CRUDDY CRUD interface because it doesnt implement delete methods and update is kind of a cop out
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public interface Servicable<T> {

    /**
     * puts a list of items somewhere
     *
     * @param tConsumer where the things go
     * @param tList list of things to consume
     */
    static <T> void createAll(Consumer<T> tConsumer, List<T> tList) {
        tList.stream().forEach(tConsumer);
    }

    /**
     * puts a single item somewhere
     *
     * @param tConsumer where the thing goes
     * @param t what needs to go somewhere
     */
    static <T> void create(Consumer<T> tConsumer, T t) {
        tConsumer.accept(t);
    }

    /**
     * read all of the items from some stream
     *
     * @param tStreamable a stream of things
     * @return a list of those things
     * @throws TigersharkException sometimes streaming things doesnt end up like oyumight expect
     */
    static <T> List<T> readAll(Streamable<T> tStreamable) throws TigersharkException {
        return tStreamable.stream().collect(Collectors.toList());
    }

    /**
     * filter out all the stuff from read all to get a single item
     *
     * @param tStreamable the things from readall
     * @param tPredicate how are you filtering
     * @return the first matching item
     * @throws TigersharkException @see readAll()
     */
    static <T> Optional<T> read(Streamable<T> tStreamable, Predicate<T> tPredicate) throws TigersharkException {
        return readAll(tStreamable).stream()
                .filter(tPredicate)
                .findFirst();
    }

    /**
     * alias for createAll()
     *
     * @param tConsumer where things go
     * @param tList list of things to consume
     */
    static <T> void updateAll(Consumer<T> tConsumer, List<T> tList) {
        createAll(tConsumer, tList);
    }

    /**
     * alias for creat()
     * @param tConsumer where things go
     * @param t a thing that needs to go
     */
    static <T> void update(Consumer<T> tConsumer, T t) {
        create(tConsumer, t);
    }

    //static <T> void deleteAll(Consumer<T> tConsumer, List<T> tList) {}

    //static <T> void delete(Consumer<T> tConsumer, T t) {}
}
