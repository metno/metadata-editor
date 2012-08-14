package no.met.metadataeditor;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ProjectListBean implements Serializable {

    private static final long serialVersionUID = -8788211171848981654L;

    
    public List<String> getProjects(){
        
        Config config = new Config("/metadataeditor.properties", Config.ENV_NAME);
        return config.getRequiredList("projects");
        
    }
    
}
