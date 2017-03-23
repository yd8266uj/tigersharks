package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Responsible for handling Library items and associated data.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class LibraryItem implements Loanable {

    /**
     *
     */
    private static final Function<Type, Long> getLoanPeriod = type -> type.compareTo(Type.BOOK) == 0 ? 3L : 1L;

    public static final Comparator<LibraryItem> sortByName = (item1, item2) -> item1.getName().compareTo(item2.getName());

    public static final Comparator<LibraryItem> sortByType = (item1, item2) -> item1.getType().compareTo(item2.getType());

    public static final Predicate<LibraryItem> isCheckedIn = item -> item.getDueDate().isPresent();

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
     * Item name
     */
    private final String name; // Not really used for anything. Ignoring get method

    /**
     * Item identifier
     */
    private final String id;

    /**
     *
     */
    private final Type type;

    /**
     *
     */
    private final Integer libraryId;

    /**
     *
     */
    private final String metadata;

    /**
     *
     */
    private LocalDate dueDate;

    /**
     *
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
    public LibraryItem(String itemId, String name, Type type, String metadata, Integer libraryId, LocalDate dueDate, String patronId) {
        this.name = name;
        this.id = itemId;
        this.type = type;
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
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type.name();
    }

    /**
     *
     * @return
     */
    public Optional<String> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    /**
     *
     * @return
     */
    public Optional<String> getPatronId() {
        return Optional.ofNullable(patronId);
    }

    /**
     *
     * @return
     */
    public Optional<Integer> getLibraryId() {
        return Optional.ofNullable(libraryId);
    }

    /**
     *
     * @return
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
     * Try to check out Loanable
     *
     * @return due date
     */
    public Optional<LocalDate> checkout(String patronId) {
        if (dueDate.compareTo(LocalDate.MIN) == 0) {
            synchronized (dueDate) {
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
     * Update Loanable state to checked in.
     *
     * @return success value
     */
    public boolean checkin() {
        if (dueDate.compareTo(LocalDate.MIN) != 0) {
            synchronized (dueDate) {
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
