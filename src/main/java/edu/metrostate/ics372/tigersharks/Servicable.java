package edu.metrostate.ics372.tigersharks;

import edu.metrostate.ics372.tigersharks.io.Streamable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by sleig on 3/16/2017.
 */
public interface Servicable<T> {

    static <T> void createAll(Consumer<T> tConsumer, List<T> tList) {
        tList.stream().forEach(tConsumer);
    }

    static <T> void create(Consumer<T> tConsumer, T t) {
        tConsumer.accept(t);
    }

    static <T> List<T> readAll(Streamable<T> tStreamable) {
        return tStreamable.stream().collect(Collectors.toList());
    }

    static <T> Optional<T> read(Streamable<T> tStreamable, Predicate<T> tPredicate) {
        return readAll(tStreamable).stream()
                .filter(tPredicate)
                .findFirst();
    }

    static <T> void updateAll(Consumer<T> tConsumer, List<T> tList) {
        createAll(tConsumer, tList);
    }

    static <T> void update(Consumer<T> tConsumer, T t) {
        create(tConsumer, t);
    }

    //static <T> void deleteAll(Consumer<T> tConsumer, List<T> tList) {}

    //static <T> void delete(Consumer<T> tConsumer, T t) {}
}
