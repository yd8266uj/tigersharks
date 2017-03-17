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

    private static final Function<Type, Long> getLoanPeriod = type -> type.compareTo(Type.BOOK) == 0 ? 3L : 1L;

    /**
     * Item name
     */
    private final String name; // Not really used for anything. Ignoring get method

    /**
     * Item identifier
     */
    private final String id;

    private final Type type;

    private final String metadata;

    private final LocalDate dueDate;

    /**
     * Item types
     */
    public enum Type {CD,DVD,BOOK,MAGAZINE}

    /**
     * Constructor
     *
     * Initialize isCheckedOut to false
     *
     * @param name item name
     * @param itemId item identifier
     */
    public LibraryItem(@NotNull String name, @NotNull String itemId, @NotNull Type type, String metadata) {
        this.name = name;
        this.id = itemId;
        this.type = type;
        this.metadata = metadata;
        this.dueDate = LocalDate.MIN;
    }

    public LibraryItem(@NotNull String name, @NotNull String itemId, @NotNull Type type) {
        this(name, itemId, type, null);
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

    public String getName() {
        return name;
    }

    public String getType() {
        return type.name();
    }

    public String getMetadata() {
        return metadata;
    }

    public Optional<LocalDate> getDueDate() {
        if (dueDate.compareTo(LocalDate.MIN) == 0) {
            synchronized (dueDate) {
                if (dueDate.compareTo(LocalDate.MIN) == 0) {
                    return Optional.of(dueDate);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Getter id
     *
     * @return item identifier
     */
    public String getId() {
        return id;
    }
}
