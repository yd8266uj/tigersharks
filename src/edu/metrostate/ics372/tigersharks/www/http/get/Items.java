package edu.metrostate.ics372.tigersharks.www.http.get;

import edu.metrostate.ics372.tigersharks.LibraryItem;

import org.watertemplate.Template;

import java.util.List;

/**
 * Created by sleig on 3/16/2017.
 */
public class Items extends Template {

    public Items(List<LibraryItem> loanables) {
        add("world","World!");
    }

    @Override
    protected String getFilePath() {
        return "items.html";
    }
}
