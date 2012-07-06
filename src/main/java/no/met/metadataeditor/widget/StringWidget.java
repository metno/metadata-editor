package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class StringWidget extends EditorWidget {

    private static final long serialVersionUID = -7542890498259559947L;

    public StringWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("str", "");
        return defaultValue;
    }

}
