package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;

/**
 * Created by sleig on 1/24/2017.
 */
abstract public class LibraryItem implements Loanable {
    private final String itemName;
    private final String itemId;
    private Boolean isCheckedOut;
    private enum Type {CD,DVD,BOOK,MAGAZINE}

    private LibraryItem(String itemName, String itemId) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.isCheckedOut = false;
    }

    public LocalDate checkout() {
        return null;
    }

    public Boolean checkin() {
        return null;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public Boolean getCheckedOut() {
        return isCheckedOut;
    }

    public static LibraryItem makeLibraryItem(String itemName, String itemId, Type type) {
        switch (type) {
            case DVD:
                return new DVD(itemName, itemId);
            case MAGAZINE:
                return new Magazine(itemName, itemId);
            default:
                return null;
        }
    }

    public static LibraryItem makeLibraryItem(String itemName, String itemId, Type type, String metadata) {
        switch (type) {
            case CD:
                return new CD(itemName, itemId, metadata);
            case BOOK:
                return new Book(itemName, itemId, metadata);
            default:
                return null;
        }
    }

    private static class Book extends LibraryItem {
        private final String itemAuthor;

        private Book(String itemName, String itemId, String itemAuthor) {
            super(itemName, itemId);
            this.itemAuthor = itemAuthor;
        }

        public String getItemAuthor() {
            return itemAuthor;
        }

        @Override
        public LocalDate checkout() {
            return null;
        }
    }

    private static class CD extends LibraryItem {
        private final String itemArtist;

        private CD(String itemName, String itemId, String itemArtist) {
            super(itemName, itemId);
            this.itemArtist = itemArtist;
        }

        public String getItemArtist() {
            return itemArtist;
        }
    }

    private static class DVD extends LibraryItem {

        private DVD(String itemName, String itemId) {
            super(itemName, itemId);
        }
    }

    private static class Magazine extends LibraryItem {

        private Magazine(String itemName, String itemId) {
            super(itemName, itemId);
        }
    }
}
