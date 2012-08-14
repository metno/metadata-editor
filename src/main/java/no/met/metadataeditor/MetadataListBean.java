package no.met.metadataeditor;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

@ManagedBean
@RequestScoped
public class MetadataListBean implements Serializable {

    private static final long serialVersionUID = 1952040514283840588L;

    private String project;
    
    public List<String> getAvailableMetadata(){
        
        DataStore datastore = DataStoreFactory.getInstance(project);
        return datastore.availableMetadata();

    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    
}
