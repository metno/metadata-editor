package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;

/**
 * Class for representing widgets. This is a pure data class and should never
 * contain any logic.
 */
public abstract class EditorWidget implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2532825684273483564L;

    // the variable name that is used for this field. Must correspond with the
    // same name in the template
    public final String variableName;
    
    public final String label;
    
    private List<Map<String,String>> values;
    
    public EditorWidget(String label, String variableName) {
        this.label = label;
        this.variableName = variableName;
        
        values = new ArrayList<Map<String,String>>();
    }

    public String getVariableName() {
        return variableName;
    }

    public String getLabel() {
        return label;
    }
    
    public List<Map<String,String>> getValues() {
        return values;
    }

    public void addValue(Map<String,String> value) {
        values.add(value);
    }
    
    public void removeValue(Map<String,String> value){        
        values.remove(value);        
    }

    public void populate(EditorVariable variable) {
        
        for(EditorVariableContent content : variable.getContent()){
            DataAttributes attrs = content.getAttrs(); 
            
            Map<String,String> value = new HashMap<String,String>();
            List<String> relevantAttrs = getRelevantAttributes();
            for( String attr : relevantAttrs ){
                value.put(attr, attrs.getAttribute(attr));    
            }
                        
            addValue(value);                    
        }        
        
    }
    
    private List<String> getRelevantAttributes() {
        
        Map<String,String> defaultValues = getDefaultValue();
        List<String> relevantAttributes = new ArrayList<String>();
        for( Map.Entry<String, String> entry : defaultValues.entrySet() ){
            relevantAttributes.add(entry.getKey());
        }
        
        return relevantAttributes;
        
    }
    
    public void addNewValue() {
        addValue(getDefaultValue());
    }
    
    public String getWidgetType() {
        return getClass().getName();
    }

    public abstract Map<String, String> getDefaultValue();
      
}
