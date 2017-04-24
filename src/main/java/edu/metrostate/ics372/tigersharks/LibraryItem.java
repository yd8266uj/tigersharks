package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Responsible for handling Library items and associated data.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class LibraryItem implements Loanable {

    /** testing...
     * get loan period in weeks based on type of item
     */
    private static final Function<Type, Long> getLoanPeriod = type -> type.compareTo(Type.BOOK) == 0 ? 3L : 1L;

    /**
     * determine relative order by name
     */
    public static final Comparator<LibraryItem> sortByName = (item1, item2) -> item1.getName().compareTo(item2.getName());


    /**
     * determine relative order by type
     */
    public static final Comparator<LibraryItem> sortByType = (item1, item2) -> item1.getType().compareTo(item2.getType());

    /**
     * determine if the item is currently checked in
     */
    public static final Predicate<LibraryItem> isCheckedIn = item -> item.getDueDate().isPresent();

    /**
     * can the item be found in this library?
     *
     * @param libraryId current library id
     * @return is it here?
     */
    public static Predicate<LibraryItem> isInLibrary(int libraryId) {
        return libraryItem -> {
            Optional<Integer> libraryIdOptional = libraryItem.getLibraryId();
            return libraryIdOptional.isPresent() && libraryIdOptional.get() == libraryId;
        };
    }


    /**
     * Item types
     */
    public enum Type {CD,DVD,BOOK,MAGAZINE}

    /**
     *Item States
     */
    public enum State {MISSING, SHELVING, REMOVED, REFERENCE}

    /**
     * Item name
     */
    private final String name; // Not really used for anything. Ignoring get method

    /**
     * Item identifier
     */
    private final String id;

    /**
     * the type of this item
     */
    private final Type type;

    /**
     * the state of this item
     */
    private State state;

    /**
     * the library this item is in
     */
    private final Integer libraryId;

    /**
     * any extra info about the item
     */
    private final String metadata;

    /**
     * when this item is due
     */
    private LocalDate dueDate;

    /**
     * Guard for non-final dueDate reference
     */
    private final Object dueDateLock = new Object();

    /**
     * who has this item checked out
     */
    private String patronId;

    /**
     * Constructor
     *
     * Initialize isCheckedOut to false
     *
     * @param name item name
     * @param itemId item identifier
     */
    public LibraryItem(String itemId, String name, Type type, State state, String metadata, Integer libraryId, LocalDate dueDate, String patronId) {
        this.name = name;
        this.id = itemId;
        this.type = type;
        this.state = state;
        this.metadata = metadata;
        this.libraryId = libraryId;
        if (dueDate == null) {
            this.dueDate = LocalDate.MIN;
        } else {
            this.dueDate = dueDate;
        }
        this.patronId = patronId;

    }

    /**
     * Getter id
     *
     * @return item identifier
     */
    public String getId() {
        return id;
    }

    /**
     * getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for type as string value
     *
     * @return type string
     */
    public String getType() {
        return type.name();
    }

    /**
     * getter for state as string value
     *
     * @return state string
     */
    public String getState(){
        return state.name();
    }

    /**
     * getter for optional metadata
     *
     * @return metadata?
     */
    public Optional<String> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    /**
     * getter for optional patron id
     *
     * @return patron id?
     */
    public Optional<String> getPatronId() {
        return Optional.ofNullable(patronId);
    }

    /**
     * getter for optional library id
     *
     * @return library id?
     */
    public Optional<Integer> getLibraryId() {
        return Optional.ofNullable(libraryId);
    }

    /**
     * getter for due date
     *
     * @return when this item is due if at all
     */
    public Optional<LocalDate> getDueDate() {
        if (dueDate.compareTo(LocalDate.MIN) == 0) {
            synchronized (dueDate) {
                if (dueDate.compareTo(LocalDate.MIN) == 0) {
                    return Optional.empty();
                }
            }
        }
        return Optional.of(dueDate);
    }

    /**
     * setterfor State
     */
    public void setState(State state){
        this.state = state;
    }


    /**
     * Try to check out item
     *
     * @return due date if there is one
     */
    public Optional<LocalDate> checkout(String patronId) {
        if (dueDate.compareTo(LocalDate.MIN) == 0) {
            synchronized (dueDateLock) {
                if (dueDate.compareTo(LocalDate.MIN) == 0) {
                    dueDate = LocalDate.now().plus(getLoanPeriod.apply(type), ChronoUnit.WEEKS); //this should be adjust
                    this.patronId = patronId;
                    return Optional.of(dueDate);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Update item state to checked in.
     *
     * @return success value
     */
    public boolean checkin() {
        if (dueDate.compareTo(LocalDate.MIN) != 0) {
            synchronized (dueDateLock) {
                if (dueDate.compareTo(LocalDate.MIN) != 0) {
                    dueDate = LocalDate.MIN;
                    this.patronId = null;
                    return true;
                }
            }
        }
        return false;
    }
}
