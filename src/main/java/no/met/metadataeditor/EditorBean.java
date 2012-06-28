package no.met.metadataeditor;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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

    public EditorBean() {

        
    }
    
    public void populate() {
        
        if(editorConfiguration == null){
            editorConfiguration = EditorConfigurationFactory.getPredefinedInstance();        
            editorConfiguration.populate(recordIdentifier);
            
            // need to get the session before the view is rendered to avoid getting exception.
            // see http://stackoverflow.com/questions/7433575/cannot-create-a-session-after-the-response-has-been-committed
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        }
    }
    
    public String save() {
        
        editorConfiguration.save(recordIdentifier);
        
        return "";
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

    public void addValue(String variableName){
        EditorWidget ew = editorConfiguration.getWidget(variableName);
        ew.addNewValue();        
    }
    
    public void removeValue(String variableName, Map<String,String> value) {
        
        EditorWidget ew = editorConfiguration.getWidget(variableName);
        ew.removeValue(value);
        
    }
    
    
}
