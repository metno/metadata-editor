package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.EditorWidget;
import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;

public class TextInput extends EditorWidget {

    /**
     * 
     */
    private static final long serialVersionUID = -7542890498259559947L;

    public TextInput(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public void populate(EditorVariable variable) {

        EditorVariableContent content = variable.getContent().get(0);
        DataAttributes attrs = content.getAttrs();
        
        Map<String,String> value = new HashMap<String,String>();
        value.put("str", attrs.getAttribute("str"));
        addValue(value);
    }

    @Override
    public void addNewValue() {
        Map<String,String> value = new HashMap<String,String>();
        value.put("str", "");
        addValue(value);
    }

}
