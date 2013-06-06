package no.met.metadataeditor.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Bean for reading the version number of the application 
 */
@ManagedBean
@ApplicationScoped
public class RevisionBean implements Serializable {
    private static final long serialVersionUID = 4259247424590809898L;
    
    private Properties props; 

    public RevisionBean() throws IOException {
        
        props = new Properties();
        props.load(RevisionBean.class.getResourceAsStream("/revision.properties"));

    }
    
    public String getRevisionNumber() {        
        return props.getProperty("revision.number");
    }
    
     public String getVersion() {        
         return props.getProperty("version");
    }
    
}
