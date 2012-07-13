package no.met.metadataeditor.datastore;

import no.met.metadataeditor.Config;

public class DataStoreFactory {

    
    public static DataStore getInstance() {
        
        Config config = new Config("/metadataeditor.properties", Config.ENV_NAME);
        
        String datastore = config.getRequired("datastore.type");
        if( "DiskDataStore".equalsIgnoreCase(datastore)){
           
            String path;
            if( config.get("datastore.path") != null ){
                path = config.get("datastore.path");
            } else {
                path = System.getProperty("java.io.tmpdir");
            }
            
            return new DiskDataStore(path);
        }
        
        return null;
        
    }
}
