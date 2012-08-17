package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class ListWidget extends EditorWidget {

    private static final long serialVersionUID = 5670896760766742704L;

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("listElement", "");    
        return defaultValue;
    }
    
}
