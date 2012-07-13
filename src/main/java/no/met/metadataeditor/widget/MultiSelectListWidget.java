package no.met.metadataeditor.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSelectListWidget extends EditorWidget {

    private static final long serialVersionUID = -7884248459977058910L;
    
    private String currentValue = "";
   
    public MultiSelectListWidget(){
        super();
    }
    
    public MultiSelectListWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("listElement", currentValue);    
        return defaultValue;
    }    
    
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
    
}
