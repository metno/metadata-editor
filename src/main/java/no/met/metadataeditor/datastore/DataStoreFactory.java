package no.met.metadataeditor.datastore;

import java.io.IOException;
import java.util.Properties;

public class DataStoreFactory {

    
    public static DataStore getInstance() {
        
        
        Properties prop = new Properties();
        try {
            prop.load(DataStore.class.getResourceAsStream("/metadataeditor.properties"));
        } catch (IOException e) {            
            throw new RuntimeException("Cannot open property file: " + e.getMessage());
        }
        
        if(prop.containsKey("datastore.type")){
            
            String datastore = prop.getProperty("datastore.type");            
            if( "DiskDataStore".equalsIgnoreCase(datastore)){
               
                String path;
                if(prop.containsKey("datastore.path")){
                    path = prop.getProperty("datastore.path");
                } else {
                    path = System.getProperty("java.io.tmpdir");
                }
                
                return new DiskDataStore(path);
            }
        } else {
            throw new RuntimeException("Missing 'datastore' in config file.");
        }
        
        return null;
        
    }
    
    
}
