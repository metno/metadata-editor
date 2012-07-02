package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorTemplateFactory;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;



/**
 * Class used accessing editor configuration that are used to generate the editor UI.
 */
public class EditorConfiguration implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6228315858621721527L;

    private static final Logger logger = Logger.getLogger(EditorConfiguration.class.getName());
    
    // list of widgets. Used to keep the order of the widgets as in the configuration
    private List<EditorWidget> widgets;
    
    private Map<String,EditorWidget> widgetMap;

    public EditorConfiguration(){
        widgets = new ArrayList<EditorWidget>();
        widgetMap = new HashMap<String,EditorWidget>();
    }

    public void addWidget(EditorWidget widget){        
        widgets.add(widget);
        widgetMap.put(widget.variableName, widget);
    }
    
    public List<EditorWidget> getWidgets(){
        return widgets;
    }
    
    public EditorWidget getWidget(String variableName){
        
        if(widgetMap.containsKey(variableName)){
            return widgetMap.get(variableName);
        }
        
        return null;
        
    }
    
    public boolean populate(String identifier) {

        EditorTemplate et = EditorTemplateFactory.getInstance(identifier);
        Map<String,EditorVariable> varMap = et.getTemplate();
        
        URL xmlUrl = EditorTemplateFactory.class.getResource("/defaultConfig/exampleMM2.xml");
        Map<String,List<EditorVariableContent>> varContent = null;
        try {
            varContent = et.getContent(new InputSource(xmlUrl.openStream()));
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for(Map.Entry<String, EditorVariable> entry : varMap.entrySet()){
            
            if(widgetMap.containsKey(entry.getKey())){
                EditorWidget widget = widgetMap.get(entry.getKey()); 
                widget.configure(entry.getValue());
                List<EditorVariableContent> content = varContent.get(entry.getKey());
                widget.populate(content);
            }
        }
        
        return allPopulated();
    }
    
    private boolean allPopulated(){
        
        List<String> notPopulated = new ArrayList<String>();
        for(Map.Entry<String,EditorWidget> entry : widgetMap.entrySet() ){
            
            if(!entry.getValue().isPopulated()){
                logger.warning("EditorWidget '" + entry.getKey() + "' has not been populated" );
                notPopulated.add(entry.getKey());
            }
        }
        
        return notPopulated.isEmpty() ? true : false; 
    }

    public void save(String identifier) {

        Map<String, List<EditorVariableContent>> content = new HashMap<String, List<EditorVariableContent>>();
        for(Entry<String, EditorWidget> entry : widgetMap.entrySet()){            
            content.put(entry.getKey(), entry.getValue().getContent());
        }
        
    }
    
    
    
}
