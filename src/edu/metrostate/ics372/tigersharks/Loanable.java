package edu.metrostate.ics372.tigersharks;

import java.time.LocalDate;

/**
 * Created by sleig on 1/29/2017.
 */
public interface Loanable {
    LocalDate checkout();
    Boolean checkin();
    String getItemId();
}
