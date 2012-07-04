package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;


public class UriWidget extends EditorWidget {

    /**
     * 
     */
    private static final long serialVersionUID = -1244778870767868773L;

    public UriWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("uri", "");
        return defaultValue;
    }


}
