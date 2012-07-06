package no.met.metadataeditor;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;
import no.met.metadataeditor.widget.EditorWidget;
import no.met.metadataeditor.widget.LatLonBoundingBoxWidget;
import no.met.metadataeditor.widget.StartAndStopTimeWidget;
import no.met.metadataeditor.widget.StringWidget;
import no.met.metadataeditor.widget.ListWidget;
import no.met.metadataeditor.widget.UriWidget;


/**
 * Factory used to create new EditorConfiguration objects. 
 */
public class EditorConfigurationFactory {

    public static EditorConfiguration getInstance(String project, String recordIdentifier){
        
        DataStore dataStore = DataStoreFactory.getInstance();
        String configString = dataStore.readEditorConfiguration(project, recordIdentifier);
        
        return unmarshallConfiguration(configString);
        
    }
    
    /**
     * Create a new editor configuration from a XML configuration file.
     * @param configString The string containing the XML editor configuration
     * @return A EditorConfiguration object.
     */
     protected static EditorConfiguration unmarshallConfiguration(String configString){

        EditorConfiguration config = null;
        try {
            JAXBContext context = JAXBContext.newInstance(EditorConfiguration.class);
            Unmarshaller um = context.createUnmarshaller();
            config = (EditorConfiguration) um.unmarshal(new StringReader(configString));
        } catch (JAXBException e) {
            throw new EditorException("Failed to create editor configuration", e);
        }

        return config;        
                
    }
    
    
    public static EditorConfiguration getPredefinedInstance(){
        
        EditorConfiguration ec = new EditorConfiguration();
        
//        EditorWidget ew1 = new StringWidget("PIName", "PIname");
//        ec.addWidget(ew1);
//
//        EditorWidget ew2 = new StringWidget("Contact", "contact");
//        ec.addWidget(ew2);
//        
//        EditorWidget ew3 = new ListWidget("Keywords", "variableList");
//        ec.addWidget(ew3);
//      
//        EditorWidget ew4 = new LatLonBoundingBoxWidget("Bounding box", "globalBB");
//        ec.addWidget(ew4);
//
//        EditorWidget ew5 = new StartAndStopTimeWidget("Time extent", "timeExtend");
//        ec.addWidget(ew5);        
//
//        EditorWidget ew6 = new UriWidget("Data ref", "dataRef");
//        ec.addWidget(ew6);        
//
//        EditorWidget ew7 = new ListWidget("Topic category", "topicCategory");
//        ec.addWidget(ew7);                
        
        
        return ec;
    }
}
