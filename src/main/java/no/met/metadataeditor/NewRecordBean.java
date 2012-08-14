package no.met.metadataeditor;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.dataTypes.EditorTemplate;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;
import no.met.metadataeditor.datastore.SupportedFormat;

@ManagedBean
@ViewScoped
public class NewRecordBean implements Serializable {

    private static final Logger logger = Logger.getLogger(NewRecordBean.class.getName());
    
    // automatically set based on the query parameters
    private String project;    
    
    // the identifier for a new record. Used when the user clicks "New"
    private String newRecordIdentifier;
    
    // the format for a new record. Used when the user clicks "New"
    private SupportedFormat newRecordFormat;    
    
    private static final long serialVersionUID = 6284081275030111665L;

    
    /**
     * Validate that the record identifier is has not been used within a project.
     * @param context Current context
     * @param component The originating component
     * @param value The new identifier value
     * @throws ValidatorException Throw if the record identifier in value is already in use
     */
    public void validateRecordIdentifier(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String identifier = (String) value;
        
        String validIdentifier = "[\\w!\"#$%&'()*+,-\\.:;<=>?@{\\|}]+";        
        if( !identifier.matches(validIdentifier)){
            FacesMessage message = new FacesMessage("Record identifier is not valid.", "Record identifier contains invalid characters. Please restrict to normal english letter, number and punctiation characters.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
        
        
        DataStore datastore = DataStoreFactory.getInstance(project);        
        if( datastore.metadataExists(identifier)){
            
            FacesMessage message = new FacesMessage("Record identifier already in use.", "The record identifier is already in use and cannot be reused for a new record.");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }                
    }      
    
    public List<SupportedFormat> getSupportedFormats() {
        
        DataStore datastore = DataStoreFactory.getInstance(project);
        return datastore.getSupportedFormats();
        
    }
    
    public String newRecord() {

        UserBean user = getUser();
        if( user.isValidated() ){
            DataStore datastore = DataStoreFactory.getInstance(project);
            String templateXML = datastore.readTemplate(newRecordFormat);
            try {
                EditorTemplate template = new EditorTemplate(new InputSource(new StringReader(templateXML)));
                Document emptyRecord = template.writeContent(new HashMap<String, List<EditorVariableContent>>());
                datastore.writeMetadata(newRecordIdentifier, EditorUtils.docToString(emptyRecord), user.getUsername(), user.getPassword());
            } catch (SAXException e) {           
                logger.log(Level.SEVERE, "Failed to parse template", e);
                throw new EditorException("Failed to parse template", e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to read template", e);
                throw new EditorException("Failed to read template", e);
            } catch (JDOMException e) {
                logger.log(Level.SEVERE, "Failed to write empty template", e);
                throw new EditorException("Failed to write template", e);            
            }        
            
            return "editor.xhtml?project=" + project + "&record=" + newRecordIdentifier + "&faces-redirect=true";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login required before creating record.", "Login required before creating record");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String getNewRecordIdentifier() {
        return newRecordIdentifier;
    }

    public void setNewRecordIdentifier(String newRecordIdentifier) {
        this.newRecordIdentifier = newRecordIdentifier;
    }

    public SupportedFormat getNewRecordFormat() {
        return newRecordFormat;
    }

    public void setNewRecordFormat(SupportedFormat newRecordFormat) {
        this.newRecordFormat = newRecordFormat;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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
