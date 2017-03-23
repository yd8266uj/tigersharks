package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.io.Streamable;
import edu.metrostate.ics372.tigersharks.support.TigersharkException;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by sleig on 3/21/2017.
 */
abstract class FileReader<T,R> implements Streamable<R> {
    protected final List<T> data;

    FileReader(InputStream inputStream) throws TigersharkException {
        this.data = getData(inputStream);
    }

    protected abstract List<T> getData(InputStream inputStream) throws TigersharkException;

    protected abstract Function<T,Optional<R>> getMap();

    @Override
    final public Stream<R> stream() {
        return data.stream().map(getMap()).filter(Optional::isPresent).map(Optional::get);
    }
}
