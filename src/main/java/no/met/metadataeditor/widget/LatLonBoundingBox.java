package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.EditorWidget;
import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;


public class LatLonBoundingBox extends EditorWidget {

    
    /**
     * 
     */
    private static final long serialVersionUID = 8770219744361367595L;

    public LatLonBoundingBox(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public void populate(EditorVariable variable) {

        EditorVariableContent content = variable.getContent().get(0);
        DataAttributes attrs = content.getAttrs();
        
        Map<String,String> value = new HashMap<String,String>();
        value.put("south", attrs.getAttribute("south"));
        value.put("north", attrs.getAttribute("south"));
        value.put("west", attrs.getAttribute("south"));
        value.put("east", attrs.getAttribute("south"));
        addValue(value);
    }
    
    public void addNewValue(){
        
        Map<String,String> value = new HashMap<String,String>();
        value.put("south", "0.0");
        value.put("north", "0.0");
        value.put("west", "0.0");
        value.put("east", "0.0");
        addValue(value);          
        
    }

}
