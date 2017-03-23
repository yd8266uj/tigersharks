package edu.metrostate.ics372.tigersharks.www.http.get;

/**
 * Created by sleig on 3/22/2017.
 */
import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

import java.util.List;
import java.util.Map;

public class MasterTemplate extends Template {

    public MasterTemplate(final String title) { // this is silly but addCollection doesnt really handle maps
        add("title", title);
    }


    @Override
    protected String getFilePath() {
        return "master_template.html";
    }
}