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
    
    public List<String> getFilteredResourceValues() {

        List<String> currentValues = new ArrayList<String>();
        List<Map<String,String>> valueMaps = getValues();
        for( Map<String, String> values : valueMaps ){
            currentValues.add(values.get("listElement"));
        }        
        
        List<String> filteredValues = this.getResourceValues();
        for( String value : currentValues ){
            filteredValues.remove(value);
        }

        return filteredValues;
    }
    
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
    
}
