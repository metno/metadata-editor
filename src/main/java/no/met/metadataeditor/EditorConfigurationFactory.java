package no.met.metadataeditor;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;


/**
 * Factory used to create new EditorConfiguration objects.
 * 
 *  EditorConfiguration objects are created by inflating XML files using JAXB.
 */
public class EditorConfigurationFactory {

    /**
     * Get an editor configuration for the record that is being edited. The editor configuration 
     * used will depend on the XML format of the metadata.s 
     * @param project The project to look of the record in.
     * @param recordIdentifier The identifier for the record that is being edited.
     * @return A EditorConfiguration object.
     */
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
    
}
