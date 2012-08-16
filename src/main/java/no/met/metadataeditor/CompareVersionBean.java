package no.met.metadataeditor;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

@ManagedBean
@ViewScoped
public class CompareVersionBean implements Serializable {

    private static final long serialVersionUID = -4719497168625095877L;
    
    private String project;
    
    private String record;


    public boolean hasVersions(){
        
        DataStore datastore = DataStoreFactory.getInstance(project);
        
        if(!datastore.metadataExists(record)){
            return false;
        }
        
        if(!datastore.metadataExists(record + DataStore.THEIRS_IDENTIFIER)){
            return false;
        }
        
        return true;
        
    }

    public String getCurrentVersion(){
        
        DataStore datastore = DataStoreFactory.getInstance(project);
        return datastore.readMetadata(record);
        
    }

    public String getTheirVersion(){
        
        DataStore datastore = DataStoreFactory.getInstance(project);
        return datastore.readMetadata(record + DataStore.THEIRS_IDENTIFIER);
        
    }
    
    public String selectCurrentVersion(){
        
        UserBean user = getUser();
        if( user.isValidated() ){
            DataStore datastore = DataStoreFactory.getInstance(project);        
            datastore.deleteMetadata(record + DataStore.THEIRS_IDENTIFIER, user.getUsername(), user.getPassword());
            FacesMessage msg = new FacesMessage("Current version selected and other version has been removed.");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);            
        } else {
            FacesMessage msg = new FacesMessage("You need to login to select version");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        return "editor.xhtml?faces-redirect=true&include-view-params=true";
    }
    
    public String selectTheirVersion(){
        
        UserBean user = getUser();
        if( user.isValidated() ){
            DataStore datastore = DataStoreFactory.getInstance(project);
            
            String otherXML = datastore.readMetadata(record + DataStore.THEIRS_IDENTIFIER);
            datastore.writeMetadata(record, otherXML, user.getUsername(), user.getPassword());            
            datastore.deleteMetadata(record + DataStore.THEIRS_IDENTIFIER, user.getUsername(), user.getPassword());
            FacesMessage msg = new FacesMessage("Version selected and written to record.");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);            
        } else {
            FacesMessage msg = new FacesMessage("You need to login to select version");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }        
        
        return "editor.xhtml?faces-redirect=true&include-view-params=true";
    }
    
    
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
    
    /**
     * @return The UserBean object for the current user.
     */
    private UserBean getUser(){
        
        // IMPLEMENTATION NOTE: This was first implemented as a @ManagedProperty, but that did not work
        // for unknown reasons. It seemed like the UserBean object changed between request even if should
        // stay the same. So this workaround was added instead.
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
        return (UserBean) request.getSession().getAttribute("userBean");
    }    
}
