package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by sleig on 3/23/2017.
 */
public class XMLElementFileReaderTest {
    private static FileReader<Element,LibraryItem> fileReader;
    private static final int LIBRARY_ID = 0;

    @Before
    public void load() throws Exception {
        InputStream is = new FileInputStream(getClass().getClassLoader().getResource("bad.xml").getFile());
        fileReader = new XMLElementFileReader(is,LIBRARY_ID);
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
        assertEquals(2,fileReader.stream().count());
    }
}