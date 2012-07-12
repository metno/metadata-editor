package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;
import no.met.metadataeditor.widget.EditorWidget;

/**
 * Simple stateless bean that is just used in the generation of the editor HTML.
 */
@ManagedBean
@ViewScoped
public class EditorBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 243543721833686400L;

    private EditorConfiguration editorConfiguration;
    
    private String recordIdentifier;
    
    private String project;
    
    boolean initPerformed = false;

    public EditorBean() {

        
    }
    
    public void init(ComponentSystemEvent event) throws IOException {

        
        
        if(!initPerformed){

            validateProject(project);
            validateRecordIdentifier(project, recordIdentifier);
            
            EditorTemplate editorTemplate = getTemplate(project,recordIdentifier);            
            editorConfiguration = EditorConfigurationFactory.getInstance(project, recordIdentifier);
            
            editorConfiguration.validateVarNames(editorTemplate);
            
            editorConfiguration.populate(project, recordIdentifier);
            editorConfiguration.addMissingOccurs();
            
            // need to get the session before the view is rendered to avoid getting exception.
            // see http://stackoverflow.com/questions/7433575/cannot-create-a-session-after-the-response-has-been-committed
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);           
            
            initPerformed = true;
        }
        
    }
    
    public EditorTemplate getTemplate(String project, String recordIdentifier){
        
        DataStore dataStore = DataStoreFactory.getInstance();
        String templateString = dataStore.readTemplate(project, recordIdentifier);
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
    
    public void populate() {
        
//        if(editorConfiguration == null){
//            editorConfiguration = EditorConfigurationFactory.getInstance(project, recordIdentifier);        
//            editorConfiguration.populate(project, recordIdentifier);
//            editorConfiguration.addMissingOccurs();
//            
//            // need to get the session before the view is rendered to avoid getting exception.
//            // see http://stackoverflow.com/questions/7433575/cannot-create-a-session-after-the-response-has-been-committed
//            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//        }
    }
    
    public void save() {
        
        editorConfiguration.save(project, recordIdentifier);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Changes has been saved.", "Changes has been saved.");
        FacesContext.getCurrentInstance().addMessage(null, msg);        
        
    }
    
    public void reset() throws IOException {
        
        if( initPerformed ){
            initPerformed = false;
            init(null);
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "All changes have been reset.", "All changes have been reset.");
            FacesContext.getCurrentInstance().addMessage(null, msg);             
        }
        
    }
    
    public void validate(){

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Validation has not been implemented yet.", "Validation has not been implemented yet.");
        FacesContext.getCurrentInstance().addMessage(null, msg);             
        
        
    }
    
    public EditorConfiguration getEditorConfiguration() {
        return editorConfiguration;
    }

    public void setEditorConfiguration(EditorConfiguration editorConfiguration) {
        this.editorConfiguration = editorConfiguration;
    }


    public String getRecordIdentifier() {
        return recordIdentifier;
    }


    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }


    public void addValue(EditorWidget widget){
        widget.addNewValue();        
    }    
       
    public void removeValue(EditorWidget widget, Map<String,String> value){
        widget.removeValue(value);
    }
    
    /**
     * Validates that the project parameter refers to an acutal project.
     * @param context
     * @param component
     * @param object
     * @throws IOException 
     */
    public void validateProject(String project) throws IOException {

        DataStore dataStore = DataStoreFactory.getInstance();
        if( !dataStore.projectExists(project)){
            String msg = "The project '" + project + "' does not exist. Please check that the project is correct.";
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(404, msg);
        }
    }

    /**
     * Validate that the record identifier exists 
     * @param context
     * @param component
     * @param object
     * @throws IOException
     */
    public void validateRecordIdentifier(String project, String recordIdentifier) throws IOException {

        DataStore dataStore = DataStoreFactory.getInstance();
        if( !dataStore.metadataExists(project, recordIdentifier)){
            String msg = "The metadata record '" + recordIdentifier + "' does not exist for the project '" + project + "'. ";
            msg += "Please check that both the record identifier and the project is correct";
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(404, msg);
        }
    }    
    
}
