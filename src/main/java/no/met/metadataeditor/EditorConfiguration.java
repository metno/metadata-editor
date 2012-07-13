package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

/**
 * Class used accessing editor configuration that are used to generate the editor UI.
 */
@XmlRootElement(name="editor", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
public class EditorConfiguration implements Serializable {
    
    private static final long serialVersionUID = -6228315858621721527L;

    private List<EditorPage> pages;
       
    public EditorConfiguration(){

        pages = new ArrayList<EditorPage>();
        
    }
    

    
    public boolean populate(String project, String identifier) {

        EditorTemplate et = getTemplate(project, identifier);    
        Map<String,List<EditorVariableContent>> varContent = getContent(project, identifier, et);
        Map<String,EditorVariable> varMap = et.getVarMap();        
        
        boolean allPopulated = true;
        for( EditorPage page : pages ) {
            boolean pagePopulated = page.populate(varMap, varContent);
            if( !pagePopulated ){
                allPopulated = false;
            }
        }
        
        return allPopulated;
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
    
    

    public void save(String project, String identifier) {

        DataStore dataStore = DataStoreFactory.getInstance();
        String templateString = dataStore.readTemplate(project, identifier);
        InputSource templateSource = new InputSource(new StringReader(templateString));
        EditorTemplate et = getTemplate(project, identifier);        
        Map<String, EditorVariable> variables = et.getVarMap();
        
        Map<String, List<EditorVariableContent>> content = new HashMap<String, List<EditorVariableContent>>();
        for( EditorPage page : pages ){
            content.putAll(page.getContent(variables));
        }
        
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


    @XmlElement(name="page", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public List<EditorPage> getPages() {
        return pages;
    }

    public void setPages(List<EditorPage> pages) {
        this.pages = pages;
    }
    
    private Map<String, EditorPage> getPageMap(){
        
        Map<String, EditorPage> pageMap = new HashMap<String,EditorPage>();
        for( EditorPage page : pages ){
            pageMap.put(page.getId(), page);
        }
        return pageMap;
        
    }
    
    public EditorPage getPage(String id){
        Map<String,EditorPage> pageMap = getPageMap();
        return pageMap.get(id);
    }
    
    public void addMissingOccurs(){
        
        for( EditorPage page : pages){
            page.addMissingOccurs();
        }
        
        
    }



    public void validateVarNames(EditorTemplate editorTemplate) {

        Map<String,EditorVariable> varMap = editorTemplate.getVarMap();
        
        for( EditorPage page : pages ){
            page.validateVarNames(varMap);
        }
        
        
        
    }
    
    
    
    
}
