package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import no.met.metadataeditor.dataTypes.EditorVariable;

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

    public abstract void populate(EditorVariable variable);
    
    public abstract void addNewValue();
    
    public String getWidgetType() {
        return getClass().getName();
    }
      
}
