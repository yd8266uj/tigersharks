package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import jdk.nashorn.internal.runtime.ECMAException;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by sleig on 3/23/2017.
 */
public class JSONObjectFileReaderTest {
    private static FileReader<JSONObject,LibraryItem> fileReader;
    private static final int LIBRARY_ID = 0;

    @Before
    public void load() throws Exception {
        InputStream is = new FileInputStream(getClass().getClassLoader().getResource("bad.json").getFile());
        fileReader = new JSONObjectFileReader(is,LIBRARY_ID);
    }

    @Test
    public void getData() throws Exception {
        assertNotNull(fileReader);
    }

    @Test
    public void getMap() throws Exception {
        fileReader.stream()
            .map(LibraryItem::getLibraryId)
            .map(Optional::get)
            .forEach(i -> assertEquals(LIBRARY_ID,i.longValue()));
        assertEquals(5,fileReader.stream().count());
    }
}