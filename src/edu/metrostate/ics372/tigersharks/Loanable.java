package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Responsible for providing a way to checkin/out library items.
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public interface Loanable {

    /**
     * Try to check out Loanable
     *
     * Calculate due date. Update Loanable state to checked out.
     *
     * @return due date
     */
    Optional<LocalDate> checkout();

    /**
     * Update Loanable state to checked in.
     *
     * @return success value
     */
    boolean checkin();
}
