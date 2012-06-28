package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.EditorWidget;
import no.met.metadataeditor.dataTypes.EditorVariable;

public class StartAndStopTimeWidget extends EditorWidget {

    /**
     * 
     */
    private static final long serialVersionUID = -6576841665492037629L;

    public StartAndStopTimeWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("start", "");
        defaultValue.put("stop", "");
        return defaultValue;
    }

}
