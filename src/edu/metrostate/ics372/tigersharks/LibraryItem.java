package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Responsible for handling Library items and associated data.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
abstract class LibraryItem implements Loanable {

    /**
     * Item name
     */
    private final String itemName; // Not really used for anything. Ignoring get method

    /**
     * Item identifier
     */
    private final String itemId;

    /**
     * Item checkout status
     */
    private AtomicBoolean isCheckedOut;

    /**
     * Item types
     */
    enum Type {CD,DVD,BOOK,MAGAZINE}

    /**
     * Constructor
     *
     * Initialize isCheckedOut to false
     *
     * @param itemName item name
     * @param itemId item identifier
     */
    private LibraryItem(String itemName, String itemId) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.isCheckedOut = new AtomicBoolean(false);
    }

    /**
     * Try to check out Loanable
     *
     * Loan period is one weeks
     *
     * @return due date
     * @see LibraryItem#checkout(long)
     */
    public LocalDate checkout() {
        return checkout(1);
    }

    /**
     * Try to check out Loanable
     *
     * @return due date
     */
    protected LocalDate checkout(long weeks) {
        if(isCheckedOut.compareAndSet(false,true)) { // is checked in? checkout and...
            return LocalDate.now().plus(weeks, ChronoUnit.WEEKS); // return a date some weeks from now
        }
        return null;
    }

    /**
     * Update Loanable state to checked in.
     *
     * @return success value
     */
    public Boolean checkin() {
        return isCheckedOut.compareAndSet(false,true); // is checked out? checkin and return true
    }

    /**
     * Getter itemId
     *
     * @return item identifier
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Static factory constructor for LibraryItem
     *
     * This LibraryItem has no metadata
     *
     * @return Concrete implementation of LibraryItem
     */
    static LibraryItem makeLibraryItem(String itemName, String itemId, Type type) {
        switch (type) {
            case DVD:
                return new DVD(itemName, itemId);
            case MAGAZINE:
                return new Magazine(itemName, itemId);
            default:
                return null;
        }
    }

    /**
     * Static factory constructor for LibraryItem
     *
     * This LibraryItem has metadata
     *
     * @return Concrete implementation of LibraryItem
     */
    static LibraryItem makeLibraryItem(String itemName, String itemId, Type type, String metadata) {
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

        /**
         * Item author name
         */
        private final String itemAuthor;

        /**
         * Constructor
         *
         * @param itemName item name
         * @param itemId item identifier
         * @param itemAuthor item author name
         * @see LibraryItem#LibraryItem(String, String)
         */
        private Book(String itemName, String itemId, String itemAuthor) {
            super(itemName, itemId);
            this.itemAuthor = itemAuthor;
        }

        /**
         * Try to check out Loanable
         *
         * Loan period is three weeks
         *
         * @return due date
         * @see LibraryItem#checkout(long)
         */
        @Override
        public LocalDate checkout() {
            return checkout(3);
        }
    }

    private static class CD extends LibraryItem {

        /**
         * Item artist name
         */
        private final String itemArtist;

        /**
         * Constructor
         *
         * @param itemName item name
         * @param itemId item identifier
         * @param itemArtist item artist name
         * @see LibraryItem#LibraryItem(String, String)
         */
        private CD(String itemName, String itemId, String itemArtist) {
            super(itemName, itemId);
            this.itemArtist = itemArtist;
        }
    }

    private static class DVD extends LibraryItem {

        /**
         * Constructor
         *
         * @param itemName item name
         * @param itemId item identifier
         * @see LibraryItem#LibraryItem(String, String)
         */
        private DVD(String itemName, String itemId) {
            super(itemName, itemId);
        }
    }

    private static class Magazine extends LibraryItem {

        /**
         * Constructor
         *
         * @param itemName item name
         * @param itemId item identifier
         * @see LibraryItem#LibraryItem(String, String)
         */
        private Magazine(String itemName, String itemId) {
            super(itemName, itemId);
        }
    }
}
