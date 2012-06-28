package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.EditorWidget;
import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.ListElementAttributes;
import no.met.metadataeditor.dataTypes.StringAttributes;

public class TextInputMulti extends EditorWidget {

    /**
     * 
     */
    private static final long serialVersionUID = 5670896760766742704L;

    public TextInputMulti(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("listElement", "");    
        return defaultValue;
    }
    
}
