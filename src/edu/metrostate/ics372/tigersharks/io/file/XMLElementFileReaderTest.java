package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import jdk.nashorn.internal.runtime.ECMAException;
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
public class XMLElementFileReaderTest {
    @Test
    public void main() throws Exception {
        final String xmlString = "<LibData>\n" +
                "<Item type=\"CD\" id=\"15dde\">\n" +
                "<Artist>Pink Floyd</Artist>\n" +
                "<Name>The Wall</Name>\n" +
                "</Item>\n" +
                "<Item type=\"DVD\" id=\"52523\">\n" +
                "<Name>Enter the Dragon</Name>\n" +
                "</Item>\n" +
                "<Item type=\"MAGAZINE\" id=\"151e5dde\">\n" +
                "<Name>Boy's Life</Name>\n" +
                "<Volume>6</Volume>\n" +
                "</Item>\n" +
                "<Item type=\"BOOK\" id=\"ern222\">\n" +
                "<Author>William Shakespeare</Author>\n" +
                "<Name>The Merchant of Venice</Name>\n" +
                "</Item>\n" +
                "</LibData>";
        InputStream stream = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
        FileReader<Element,LibraryItem> fileReader = new XMLElementFileReader(stream,1);
        List<LibraryItem> libraryItemList = fileReader.stream().collect(Collectors.toList());
        assertEquals(libraryItemList.size(),4);
    }

}