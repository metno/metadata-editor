package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

public class Editor implements Serializable {

    private static final long serialVersionUID = -5065862868177223093L;
    
    private String project;
    
    private String recordIdentifier;
    
    private EditorConfiguration editorConfiguration;
    
    public Editor(String project, String recordIdentifier){

        this.project = project;
        this.recordIdentifier = recordIdentifier;
        
               
    }
    
    public EditorConfiguration getEditorConfiguration() {
        return editorConfiguration;
    }

    public void setEditorConfiguration(EditorConfiguration editorConfiguration) {
        this.editorConfiguration = editorConfiguration;
    }    
    
    public boolean init() {
        
        EditorTemplate editorTemplate = getTemplate(project,recordIdentifier);            
        editorConfiguration = EditorConfigurationFactory.getInstance(project, recordIdentifier);        
        editorConfiguration.validateVarNames(editorTemplate);
        
        EditorTemplate et = getTemplate(project, recordIdentifier);    
        Map<String,List<EditorVariableContent>> contentMap = getContent(project, recordIdentifier, et);
        Map<String,EditorVariable> varMap = et.getVarMap();           
        
        editorConfiguration.configure(varMap);
        editorConfiguration.generateEditorWidgetViews(contentMap);
        editorConfiguration.addMissingOccurs();        
        
        return true;
    }
    

    /**
     * Save the contents of the editor to the datastore.
     * @param project The name of the current project.
     * @param identifier The identifier of the current record.
     */
    public void save(String project, String identifier, String username, String password) {

        String xml = editorContentToXML(project, identifier);        
        DataStore dataStore = DataStoreFactory.getInstance(project);
        dataStore.writeMetadata(identifier, xml, username, password);
        
    }

    /**
     * @param project The name of the current project
     * @param identifier The identifier of the current record.
     * @return The XML contents of the editor in its current state.
     */
    public String export(String project, String identifier) {

        String xml = editorContentToXML(project, identifier);
        return xml;
    }        

    
    private String editorContentToXML(String project, String identifier){

        EditorTemplate et = getTemplate(project, identifier);        
        Map<String, EditorVariable> varMap = et.getVarMap();
        
        Map<String, List<EditorVariableContent>> content = editorConfiguration.getContent(varMap);
        
        String resultString;
        try {
            Document resultDoc = et.writeContent(content);
            resultString = EditorUtils.docToString(resultDoc);
            
        } catch (JDOMException e) {
            throw new EditorException(e.getMessage());
        } catch (IOException e) {
            throw new EditorException(e.getMessage());
        }
        
        return resultString;
        
        
        
    }
    
    
    private EditorTemplate getTemplate(String project, String identifier){
        
        DataStore dataStore = DataStoreFactory.getInstance(project);
        String templateString = dataStore.readTemplate(identifier);
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
        
        DataStore dataStore = DataStoreFactory.getInstance(project);
        String metadataString = dataStore.readMetadata(identifier);
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

}
