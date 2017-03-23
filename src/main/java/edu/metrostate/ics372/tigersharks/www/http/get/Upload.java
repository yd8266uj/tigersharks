package edu.metrostate.ics372.tigersharks.www.http.get;

import org.watertemplate.Template;

/**
 * renders an upload form page
 *
 * @author tigersharks <a href="https://github.com/yd8266uj/tigersharks">github</a>
 * @version 1
 */
public class Upload extends Template {

    private final String title = "upload";

    public Upload(String libraryId) {
        add("libraryId",libraryId);
    }

    @Override
    protected Template getMasterTemplate() {
        return new MasterTemplate(title);
    }

    @Override
    protected String getFilePath() {
        return "upload.html";
    }
}
