package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.met.metadataeditor.widget.EditorWidget;

public class EditorWidgetView implements Serializable {

    private static final long serialVersionUID = 4577883892935459131L;
    
    private Map<String, String> values = new HashMap<String, String>();
    
    private List<EditorWidget> children = new ArrayList<EditorWidget>();
    
    private Map<String, EditorWidget> childMap = new HashMap<String,EditorWidget>();
          
    public EditorWidgetView(){
        
    }
        
    public void addValue(String varName, String value) {
        values.put(varName, value);
    }    
        
    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public List<EditorWidget> getChildren() {
        return children;
    }

    public void setChildren(List<EditorWidget> children) {
        this.children = children;
        
        childMap.clear();
        for( EditorWidget child : this.children ){
            childMap.put(child.getVariableName(), child);
        }
        
    }
    
    public boolean hasChildWidget(String varName){
        return childMap.containsKey(varName);
    }
    
    public EditorWidget getChildWidget(String varName){
        return childMap.get(varName);
    }
}
