package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorTemplateFactory;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;



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
    
    public boolean populate(String project, String identifier) {

        EditorTemplate et = getTemplate(project, identifier);    
        Map<String,List<EditorVariableContent>> varContent = getContent(project, identifier, et);
        Map<String,EditorVariable> varMap = et.getTemplate();        
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
    
    private EditorTemplate getTemplate(String project, String identifier){
        DataStore dataStore = DataStoreFactory.getInstance();
        String templateString = dataStore.readTemplate(project, identifier);
        InputSource templateSource = new InputSource(new StringReader(templateString));
        
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(templateSource);
        } catch (SAXException e) {
            throw new EditorException(e.getMessage());
        } catch (IOException e) {
            throw new EditorException(e.getMessage());
        } 
        return et;
    }
    
    public Map<String,List<EditorVariableContent>> getContent(String project, String identifier, EditorTemplate template){
        
        DataStore dataStore = DataStoreFactory.getInstance();
        String metadataString = dataStore.readMetadata(project, identifier);
        Map<String,List<EditorVariableContent>> varContent = null;
        try {
            varContent = template.getContent(new InputSource(new StringReader(metadataString)));
        } catch (ParserConfigurationException e) {
            throw new EditorException(e.getMessage());
        } catch (SAXException e) {
            throw new EditorException(e.getMessage());
        } catch (IOException e) {
            throw new EditorException(e.getMessage());
        }      
        return varContent;        
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

    public void save(String project, String identifier) {

        Map<String, List<EditorVariableContent>> content = new HashMap<String, List<EditorVariableContent>>();
        for(Entry<String, EditorWidget> entry : widgetMap.entrySet()){            
            content.put(entry.getKey(), entry.getValue().getContent());
        }
        
        DataStore dataStore = DataStoreFactory.getInstance();
        String templateString = dataStore.readTemplate(project, identifier);
        InputSource templateSource = new InputSource(new StringReader(templateString));
        
        EditorTemplate et = getTemplate(project, identifier);        
        try {
            Document resultDoc = et.writeContent(templateSource, content);
            String resultString = docToString(resultDoc);
            dataStore.writeMetadata(project, identifier, resultString);
            
        } catch (JDOMException e) {
            throw new EditorException(e.getMessage());
        } catch (IOException e) {
            throw new EditorException(e.getMessage());
        }
        
    }
    
    private String docToString(Document doc) throws IOException {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        xout.output(doc, writer);
        return writer.toString();
    }
    
    
    
}
