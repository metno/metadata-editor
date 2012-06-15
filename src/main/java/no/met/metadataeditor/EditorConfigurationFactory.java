package no.met.metadataeditor;

import java.io.File;

import no.met.metadataeditor.widget.TextInputWidget;

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
        
        TextInputWidget tiw = new TextInputWidget("widget1");
        tiw.setValue("MM2 hurra1");
        ec.addWidget(tiw);
        
        return ec;
    }
}
