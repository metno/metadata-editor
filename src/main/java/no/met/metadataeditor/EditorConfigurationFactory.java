package no.met.metadataeditor;

import java.io.File;

import no.met.metadataeditor.widget.LatLonBoundingBoxWidget;
import no.met.metadataeditor.widget.StartAndStopTimeWidget;
import no.met.metadataeditor.widget.StringWidget;
import no.met.metadataeditor.widget.ListWidget;


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
        
        EditorWidget ew1 = new StringWidget("PIName", "PIName");
        ec.addWidget(ew1);

        EditorWidget ew2 = new StringWidget("Contact", "contact");
        ec.addWidget(ew2);
        
        EditorWidget ew3 = new ListWidget("Keywords", "variableList");
        ec.addWidget(ew3);
        
        EditorWidget ew4 = new LatLonBoundingBoxWidget("Bounding box", "localBB");
        ec.addWidget(ew4);

        EditorWidget ew5 = new StartAndStopTimeWidget("Time extent", "timeExtend");
        ec.addWidget(ew5);        
        
        return ec;
    }
}
