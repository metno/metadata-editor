package no.met.metadataeditor;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

/**
 * Bean for holding the current user state and for validating the user with the server. 
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 7254151747383899821L;

    // the username for the current user. The variable is transient to prevent writing to disk
    private transient String username;

    // the password for the current user. The variable is transient to prevent writing to disk
    // We store the password so that we can use it for authentication with the underlying data store on writing.
    private transient String password;

    private transient boolean validated = false;
    
    public UserBean() {

    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    /**
     * Check the users credentials with the DataStore for a project.
     * 
     * Sets the state of the bean if the user credentials are ok.
     */
    public void validateCredentials(){
        
        String project = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("project");
        
        // if validate set to validated
        DataStore datastore = DataStoreFactory.getInstance(project);
        if(datastore.userHasWriteAccess(username, password)){
            
            validated = true;
            FacesMessage msg = new FacesMessage("Login successfull!");
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg);            
            
            
        } else {
            FacesMessage msg = new FacesMessage("Invalid username or password");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);            
        }
        
    }
    
    /**
     * Do nothing. This function is used to close the login dialog on success and
     * cause a re-rendering of the page.
     */
    public void closeLoginDialog() {
        //this is a no-op, but it simplified some JSF code to have it.
    }
    
    public void logout(){
        validated = false;
        username = null;
        password = null;
               
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean isValidated) {
        this.validated = isValidated;
    }
    
}
