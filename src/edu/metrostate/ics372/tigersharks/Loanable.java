package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;

/**
 * Created by sleig on 1/29/2017.
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
