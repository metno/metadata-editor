package no.met.metadataeditor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Simple stateless bean that is just used in the generation of the editor HTML.
 */
@ManagedBean
@RequestScoped
public class EditorBean {

    private EditorConfiguration editorConfiguration;

    public EditorBean() {
        editorConfiguration = EditorConfigurationFactory.getPredefinedInstance();
    }
    
    
    public EditorConfiguration getEditorConfiguration() {
        return editorConfiguration;
    }

    public void setEditorConfiguration(EditorConfiguration editorConfiguration) {
        this.editorConfiguration = editorConfiguration;
    }
    
    
}
