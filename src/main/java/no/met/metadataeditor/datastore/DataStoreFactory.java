package no.met.metadataeditor.datastore;

import no.met.metadataeditor.Config;

/**
 * Factory class for creating new DataStore objects. 
 */
public class DataStoreFactory {

    
    /**
     * @return A DataStore instance based on the values found in the metadata editor configuration.
     */
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
        } else if ( "WebDAVDataStore".equalsIgnoreCase(datastore)){
            
            String protocol = config.getRequired("datastore.protocol");
            String host = config.getRequired("datastore.host");
            String defaultUser = config.getRequired("datastore.defaultUser");
            String defaultPassword = config.getRequired("datastore.defaultPassword");
            return new WebDAVDataStore(protocol, host, defaultUser, defaultPassword); 
            
        }
        
        return null;
        
    }
}
