package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;

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
    LocalDate checkout();

    /**
     * Update Loanable state to checked in.
     *
     * @return success value
     */
    Boolean checkin();

    /**
     * Getter itemId
     *
     * @return item identifier
     */
    String getItemId();
}
