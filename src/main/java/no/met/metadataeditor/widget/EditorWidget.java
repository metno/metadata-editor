package no.met.metadataeditor.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    private boolean isPopulated = false;
    
    private int maxOccurs = 1;
    
    private int minOccurs = 1;
    
    private List<String> resourceValues;
    
    // the type of data attributes. Used to construct new objects of the same type.
    private DataAttributes attributesType;
    
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

    public void configure(EditorVariable variable){
        maxOccurs = variable.getMaxOccurs();
        minOccurs = variable.getMinOccurs();
        
        if( variable.hasDefaultResource() ){
            resourceValues = variable.getDefaultResourceValues();
        }
        attributesType = variable.getNewDataAttributes();
    }
    
    public void populate(List<EditorVariableContent> contents) {
        
        for(EditorVariableContent content : contents){
            DataAttributes attrs = content.getAttrs(); 
            
            Map<String,String> value = new HashMap<String,String>();
            List<String> relevantAttrs = getRelevantAttributes();
            for( String attr : relevantAttrs ){
                value.put(attr, attrs.getAttribute(attr));    
            }
                        
            addValue(value);                    
        }
        
        isPopulated = true;
        
    }
    
    /**
     * Take the information stored in the widget and send it back to the EditorVariable
     */
    public List<EditorVariableContent> getContent(){
        
        List<EditorVariableContent> contentList = new ArrayList<EditorVariableContent>();
        for( Map<String,String> value : values ){

            EditorVariableContent content = new EditorVariableContent();
            DataAttributes da = attributesType.newInstance();
            content.setAttrs(da);            
            for( Map.Entry<String, String> entry : value.entrySet() ){
                da.addAttribute(entry.getKey(), entry.getValue());
            }
            contentList.add(content);            
        }
        
        return contentList;
        
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

    public List<String> getResourceValues(){
        return resourceValues;
    }
    
    public int getMaxOccurs(){
        return maxOccurs;
    }

    public int getMinOccurs(){
        return minOccurs;
    }
    
    public boolean isPopulated(){
        return isPopulated; 
    }
    
    public abstract Map<String, String> getDefaultValue();
      
}
