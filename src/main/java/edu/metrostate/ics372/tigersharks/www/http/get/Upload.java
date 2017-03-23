package edu.metrostate.ics372.tigersharks.www.http.get;

import org.watertemplate.Template;

/**
 * Created by sleig on 3/16/2017.
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
