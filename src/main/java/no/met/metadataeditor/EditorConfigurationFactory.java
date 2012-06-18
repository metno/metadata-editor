package no.met.metadataeditor;

import java.io.File;

import no.met.metadataeditor.widget.EditorWidget;

/**
 * Factory used to create new EditorConfiguration objects. 
 */
public class EditorConfigurationFactory {

    /**
     * Create a new editor configuration from a XML configuration file.
     * @param configuration The path to the configuration file.
     * @return A EditorConfiguration object.
     */
    public static EditorConfiguration getInstance(File configuration){
        return null;
    }
    
    
    public static EditorConfiguration getPredefinedInstance(){
        
        EditorConfiguration ec = new EditorConfiguration();
        
        EditorWidget ew1 = new EditorWidget("PIName", "TextInput");
        ec.addWidget(ew1);

        EditorWidget ew2 = new EditorWidget("contact", "TextInput");
        ec.addWidget(ew2);
        
        EditorWidget ew3 = new EditorWidget("variableList", "TextInputMulti");
        ec.addWidget(ew3);
        
        return ec;
    }
}
