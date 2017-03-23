package edu.metrostate.ics372.tigersharks.www.http.get;

import org.watertemplate.Template;

/**
 * renders a homepage
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
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


