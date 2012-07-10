package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.InvalidTemplateException;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.widget.EditorWidget;

public class EditorPage implements Serializable {

    private static final long serialVersionUID = 512799681077291836L;

    private String label;
    
    private String id;

    private List<EditorWidget> widgets = new ArrayList<EditorWidget>();
    
    public EditorPage() {

    }

    @XmlAttribute(required=true)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute(required=true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name="widget", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public List<EditorWidget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<EditorWidget> widgets) {
        this.widgets = widgets;
        //updateWidgetMap(this.widgets);
    }
    
    private Map<String, EditorWidget> getWidgetMap(){
        
        Map<String,EditorWidget> widgetMap = new HashMap<String,EditorWidget>();
        for( EditorWidget widget : widgets ){
            widgetMap.put(widget.getVariableName(), widget);
        }
        return widgetMap;
        
    }
    
    public EditorWidget getWidget(String variableName){
        
        Map<String,EditorWidget> widgetMap = getWidgetMap();
        if(widgetMap.containsKey(variableName)){
            return widgetMap.get(variableName);
        }
        
        return null;
        
    }
    
    public boolean populate(String project, DataStore dataStore, Map<String, EditorVariable> varMap, Map<String, List<EditorVariableContent>> contentMap){
        
        Map<String,EditorWidget> widgetMap = getWidgetMap();
        for(Map.Entry<String, EditorVariable> entry : varMap.entrySet()){
            
            if(widgetMap.containsKey(entry.getKey())){
                EditorWidget widget = widgetMap.get(entry.getKey()); 
                widget.configure(project, dataStore, entry.getValue());
                List<EditorVariableContent> content = contentMap.get(entry.getKey());
                widget.populate(content);
            }
        }
        
        return allPopulated();
       
        
    }
    
    private boolean allPopulated(){
        
        Map<String,EditorWidget> widgetMap = getWidgetMap();
        List<String> notPopulated = new ArrayList<String>();
        for(Map.Entry<String,EditorWidget> entry : widgetMap.entrySet() ){
            
            if(!entry.getValue().isPopulated()){
                Logger.getLogger(getClass().getName()).warning("EditorWidget '" + entry.getKey() + "' has not been populated" );
                notPopulated.add(entry.getKey());
            }
        }
        
        return notPopulated.isEmpty() ? true : false; 
    }
    
    public Map<String, List<EditorVariableContent>> getContent(Map<String, EditorVariable> variables){
        
        Map<String,EditorWidget> widgetMap = getWidgetMap();
        Map<String, List<EditorVariableContent>> content = new HashMap<String, List<EditorVariableContent>>();
        for(Entry<String, EditorWidget> entry : widgetMap.entrySet()){  
            EditorVariable ev = variables.get(entry.getKey());
            
            content.put(entry.getKey(), entry.getValue().getContent(ev));
        }
        
        return content;
    }

    public void addMissingOccurs() {

        for( EditorWidget widget : widgets ){
            widget.addMissingOccurs();
        }
        
        
    }

    public void validateVarNames(Map<String, EditorVariable> varMap) {

        for( EditorWidget widget : widgets ){
            if(! varMap.containsKey(widget.getVariableName())){
                throw new InvalidEditorConfigurationException("The variable '" + widget.getVariableName() + "' was not found in the template");
            }
        }
        
    }
}
