package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;
import no.met.metadataeditor.datastore.DataStoreUtils;
import no.met.metadataeditor.datastore.SupportedFormat;
import no.met.metadataeditor.widget.EditorWidget;

/**
 * Bean used to the hold the current state of the editor.
 * 
 * The bean is in view scope.
 */
@ManagedBean
@ViewScoped
public class EditorBean implements Serializable {

    private static final long serialVersionUID = 243543721833686400L;

    private Editor editor;
    
    // automatically set based on the query parameters
    private String recordIdentifier;
    
    // automatically set based on the query parameters
    private String project;
    
    // used to track the current active tab. We need this as some times the entire form is re-rendered and we lose
    // the current tab wihtout this
    private int activeTabId = 0;
    
    boolean initPerformed = false;
    
    @ManagedProperty(value="#{userBean}")
    private UserBean user;
    
    
    public EditorBean() {

        
    }

    /**
     * Initialise the the bean based on the "project" and "recordIdentifier".
     * This method is automatically called in the preRenderView phase.
     * @param event
     * @throws IOException
     */
    public void init(ComponentSystemEvent event) throws IOException {

        if(!initPerformed){

            validateProject(project);
            validateRecordIdentifier(project, recordIdentifier);

            editor = new Editor(project, recordIdentifier);
            editor.init();
                        
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
    
    public void save() {
        
        if(user.isValidated()){        
            editor.save(project, recordIdentifier, user.getUsername(), user.getPassword());

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Changes has been saved.", "Changes has been saved.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login required before saving.", "Login required before saving.");
            FacesContext.getCurrentInstance().addMessage(null, msg);            
        }
        
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
    
    public void export() {
        
        String editorContent = editor.export(project, recordIdentifier);
        

        FacesContext ctx = FacesContext.getCurrentInstance();
        final HttpServletResponse resp = (HttpServletResponse)ctx.getExternalContext().getResponse();

        resp.setContentType("application/octet-stream");
        resp.setContentLength(editorContent.length());
        resp.setHeader( "Content-Disposition", "attachment;filename=" + recordIdentifier + ".xml" );
        try {
            resp.getOutputStream().write(editorContent.getBytes());
            resp.getOutputStream().flush();
            resp.getOutputStream().close();            
        } catch (IOException e) {
            throw new EditorException("Failed to write XML to response", e);
        }

        ctx.responseComplete();
        
    }
    
    
    public EditorConfiguration getEditorConfiguration() {
        return editor.getEditorConfiguration();
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


    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getActiveTabId() {
        return activeTabId;
    }

    public void setActiveTabId(int activeTabId) {
        this.activeTabId = activeTabId;
    }
    
    /**
     * Track that the currently selected tab has changed.
     * @param event
     */
    public void tabChanged(TabChangeEvent event){  
        TabView tv = (TabView)event.getTab().getParent();
        activeTabId = tv.getActiveIndex();
    }    

    public void addValue(EditorWidget widget){
        widget.addNewValue();        
    }    
       
    public void removeValue(EditorWidget widget, Map<String,String> value){
        widget.removeValue(value);
    }
    
    public List<String> getResourceValues(EditorWidget widget){
        
        DataStore dataStore = DataStoreFactory.getInstance();
        String resourceString = dataStore.readResource(project, widget.getResourceUri().toString());

        String[] resourceValues = resourceString.split("\n");
        List<String> values = new ArrayList<String>();
        for(String s : resourceValues ){
            values.add(s);
        }
        return values;        
        
    }
    
    public List<String> getFilteredResourceValues(EditorWidget widget, String filterAttribute) {

        List<String> currentValues = new ArrayList<String>();
        List<Map<String,String>> valueMaps = widget.getValues();
        for( Map<String, String> values : valueMaps ){
            currentValues.add(values.get(filterAttribute));
        }        
        
        List<String> filteredValues = getResourceValues(widget);
        for( String value : currentValues ){
            filteredValues.remove(value);
        }

        return filteredValues;
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
