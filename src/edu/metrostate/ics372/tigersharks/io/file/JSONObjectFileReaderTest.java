package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by sleig on 3/21/2017.
 */
public class JSONObjectFileReaderTest {
    @Test
    public void main() throws Exception {
        final String jsonString = "{\r\n \"library_items\":[\r\n {\r\n \"item_name\":\"Yellow Submarine\",\r\n \"item_type\":\"CD\",\r\n \"item_id\":\"48934j\",\r\n \"item_artist\":\"Beatles\"\r\n },\r\n {\r\n \"item_name\":\"The Count of Monte Cristo\",\r\n \"item_type\":\"Book\",\r\n \"item_id\":\"1adf4\",\r\n \"item_author\":\"Alexandre Dumas\"\r\n },\r\n {\r\n \"item_name\":\"Cat Fancy v23\",\r\n \"item_type\":\"Magazine\",\r\n \"item_id\":\"1a545\"\r\n },\r\n {\r\n \"item_name\":\"Toy Story\",\r\n \"item_type\":\"DVD\",\r\n \"item_id\":\"85545\"\r\n }\r\n ]\r\n}";
        InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        FileReader<JSONObject,LibraryItem> fileReader = new JSONObjectFileReader(stream,1);
        List<LibraryItem> libraryItemList = fileReader.stream().collect(Collectors.toList());
        assertEquals(libraryItemList.size(),4);
    }
}