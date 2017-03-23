package edu.metrostate.ics372.tigersharks.www.http.get;

import org.watertemplate.Template;

/**
 * Created by sleig on 3/22/2017.
 */
public class Home extends Template {
    private final String title = "Home";

    public Home() {
    }

    @Override
    protected Template getMasterTemplate() {
        return new MasterTemplate(title);
    }

    @Override
    protected String getFilePath() {
        return "home.html";
    }
}


