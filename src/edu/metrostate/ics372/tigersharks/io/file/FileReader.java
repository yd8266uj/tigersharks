package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.io.Streamable;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/21/2017.
 */
abstract class FileReader<T,R> implements Streamable<R> {
    protected final List<T> data;

    FileReader(InputStream inputStream) {
        this.data = getData(inputStream);
    }

    protected abstract List<T> getData(InputStream inputStream);

    protected abstract Function<T,R> getMap();

    @Override
    final public Stream<R> stream() {
        return data.stream().map(getMap());
    }
}
