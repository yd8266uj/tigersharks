package edu.metrostate.ics372.tigersharks;

import com.sun.istack.internal.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

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
    private final LocalDate dueDate;

    /**
     *
     */
    private final Integer patronId;

    /**
     * Constructor
     *
     * Initialize isCheckedOut to false
     *
     * @param name item name
     * @param itemId item identifier
     */
    public LibraryItem(@NotNull String itemId, @NotNull String name, @NotNull Type type, String metadata, Integer libraryId, LocalDate dueDate, Integer patronId) {
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
     *
     * @param itemId
     * @param name
     * @param type
     * @param metadata
     */
    public LibraryItem(@NotNull String itemId, @NotNull String name, @NotNull Type type, String metadata) {
        this(itemId, name, type, null, null, null, null);
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
        return Optional.of(metadata);
    }

    /**
     *
     * @return
     */
    public Optional<Integer> getPatronId() {
        return Optional.of(patronId);
    }

    /**
     *
     * @return
     */
    public Optional<Integer> getLibraryId() {
        return Optional.of(libraryId);
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
    public Optional<LocalDate> checkout() {
        if (dueDate.compareTo(LocalDate.MIN) == 0) {
            synchronized (dueDate) {
                if (dueDate.compareTo(LocalDate.MIN) == 0) {
                    dueDate.adjustInto(LocalDate.now().plus(getLoanPeriod.apply(type), ChronoUnit.WEEKS));
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
                    dueDate.adjustInto(LocalDate.MIN);
                    return true;
                }
            }
        }
        return false;
    }
}
