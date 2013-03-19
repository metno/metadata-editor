package no.met.metadataeditor.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

@ManagedBean
@ViewScoped
public class MetadataListBean implements Serializable {

    private static final long serialVersionUID = 1952040514283840588L;

    private String project;
    
    private List<String> availableMetadata;
    
    public List<String> getAvailableMetadata(){
        
        if( availableMetadata == null ){
            DataStore datastore = DataStoreFactory.getInstance(project);
            availableMetadata = datastore.availableMetadata();
        }
        return availableMetadata;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    
}
