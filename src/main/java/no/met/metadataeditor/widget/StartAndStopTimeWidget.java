package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class StartAndStopTimeWidget extends EditorWidget {

    private static final long serialVersionUID = -6576841665492037629L;

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("start", "");
        defaultValue.put("stop", "");
        return defaultValue;
    }

}
