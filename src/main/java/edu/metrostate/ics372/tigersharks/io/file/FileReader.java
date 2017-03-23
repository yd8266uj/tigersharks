package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.io.Streamable;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
*FileReader 
*
* @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * 
 */
abstract class FileReader<T,R> implements Streamable<R> {
    protected final List<T> data;

    /*
    *
    *Sets value for variable data
    */
    FileReader(InputStream inputStream) {
        this.data = getData(inputStream);
    }

    protected abstract List<T> getData(InputStream inputStream);

    protected abstract Function<T,R> getMap();
    
    /*
    *
    *return map for data
    */
    @Override
    final public Stream<R> stream() {
        return data.stream().map(getMap());
    }
}
