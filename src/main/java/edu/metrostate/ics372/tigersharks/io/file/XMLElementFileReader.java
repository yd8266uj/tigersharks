package edu.metrostate.ics372.tigersharks.io.file;

import edu.metrostate.ics372.tigersharks.LibraryItem;
import edu.metrostate.ics372.tigersharks.support.TigersharkException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Responsible for generating a supply of Loanable items from an XML file
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 */
public class XMLElementFileReader extends FileReader<Element, LibraryItem> {

    /*  */
    private static final String ITEM_TAGNAME = "Item";
    private static final String TYPE_ATTRIBUTENAME = "type";
    private static final String ID_ATTRIBUTENAME = "id";
    private static final String NAME_TAGNAME = "Name";
    private static final String METADATA_TAGNAME_CD = "Artist";
    private static final String METADATA_TAGNAME_BOOK = "Author";
    private static final String METADATA_TAGNAME_MAGAZINE = "Volume";

    /**
     *
     */
    private final int libraryId;

    /**
     *
     * @param inputStream
     * @param libraryId
     */
    public XMLElementFileReader(InputStream inputStream, int libraryId) throws TigersharkException {
        super(inputStream);
        this.libraryId = libraryId;
    }
    /**
    * Create a DocumentBuilder, and create document from XML file
    */
    @Override
    protected List<Element> getData(InputStream inputStream) throws TigersharkException {
        List<Element> elementList = new ArrayList<>();
        // use inputStream to get a list of Element and then return it
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(ITEM_TAGNAME);
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    elementList.add(element);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return elementList;
    }
    /**
    *  Examine attributes of the element, compare to cases and create
    *  new LibraryItem object.
    */
    @Override
    protected Function<Element, Optional<LibraryItem>> getMap() {
        return element -> {
            /*  */
            if (!element.hasAttribute(ID_ATTRIBUTENAME) || !element.hasAttribute(TYPE_ATTRIBUTENAME)) {
                return Optional.empty();
            }

            final String id = element.getAttribute(ID_ATTRIBUTENAME).trim();
            if(id.equals("")) {
                return Optional.empty();
            }

            NodeList nodeList = element.getElementsByTagName(NAME_TAGNAME);
            if(nodeList == null || nodeList.item(0) == null) {
                return Optional.empty();
            }

            final String name = nodeList.item(0).getTextContent().trim();
            if(name.equals("")) {
                return Optional.empty();
            }

            final LibraryItem.Type type;
            try {
                type = LibraryItem.Type.valueOf(element.getAttribute(TYPE_ATTRIBUTENAME).toUpperCase());
            } catch (Exception e) {
                return Optional.empty();
            }

            final String metadata;
            /*  */
            if (type.compareTo(LibraryItem.Type.CD) == 0) {
                nodeList = element.getElementsByTagName(METADATA_TAGNAME_CD);
                if(nodeList == null || nodeList.item(0) == null) {
                    return Optional.empty();
                }
                metadata = nodeList.item(0).getTextContent().trim();
                if(metadata.equals("")) {
                    return Optional.empty();
                }
            } else if (type.compareTo(LibraryItem.Type.BOOK) == 0) {
                nodeList = element.getElementsByTagName(METADATA_TAGNAME_BOOK);
                if(nodeList == null || nodeList.item(0) == null) {
                    return Optional.empty();
                }
                metadata = nodeList.item(0).getTextContent().trim();
                if(metadata.equals("")) {
                    return Optional.empty();
                }
            } else if (type.compareTo(LibraryItem.Type.MAGAZINE) == 0) {
                nodeList = element.getElementsByTagName(METADATA_TAGNAME_MAGAZINE);
                if(nodeList == null || nodeList.item(0) == null) {
                    return Optional.empty();
                }
                metadata = nodeList.item(0).getTextContent().trim();
                if(metadata.equals("")) {
                    return Optional.empty();
                }
            } else {
                metadata = null;
            }

            return Optional.of(new LibraryItem(id, name, type, metadata, libraryId, null, null)); //
        };
    }
}
