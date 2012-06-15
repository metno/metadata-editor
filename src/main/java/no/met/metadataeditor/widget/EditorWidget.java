package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for representing widgets. This is a pure data class and should never
 * contain any logic.
 */
public abstract class EditorWidget {

    // the variable name that is used for this field. Must correspond with the
    // same name in the template
    public final String widgetId;
   
    // mapping between the widget variables and the current values for each widget.
    private Map<String,String> values;

    public EditorWidget(String widgetId) {
        this.widgetId = widgetId;
        this.values = new HashMap<String,String>();
        
    }

    protected String getValue(String varName){
        
        if(!values.containsKey(varName)){
            throw new IllegalArgumentException("This editor widget does not contain the variable: " + varName);
        }
        
        return values.get(varName);
    }
    
    protected void setValue(String varName, String value){
        values.put(varName, value);
    }

    public abstract String getType();

    public String getWidgetId() {
        return widgetId;
    }
    
}
