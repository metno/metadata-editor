package no.met.metadataeditor.datastore;

import java.util.List;

import no.met.metadataeditor.Config;
import no.met.metadataeditor.EditorException;

/**
 * Factory class for creating new DataStore objects. 
 */
public class DataStoreFactory {

    
    /**
     * @return A DataStore instance based on the values found in the metadata editor configuration.
     */
    public static DataStore getInstance(String project) {
        
        Config config = new Config("/metadataeditor.properties", Config.ENV_NAME);
        
        if(!projectConfigured(project, config)){
            throw new EditorException("Project has not been configured: " + project );
        }
        
        
        String datastore = config.getRequired(project + ".datastore.type");
        if( "DiskDataStore".equalsIgnoreCase(datastore)){
           
            String path;
            if( config.get(project + ".datastore.path") != null ){
                path = config.get(project + ".datastore.path");
            } else {
                path = System.getProperty("java.io.tmpdir");
            }
            
            return new DiskDataStore(path);
        } else if ( "WebDAVDataStore".equalsIgnoreCase(datastore)){
            
            String protocol = config.getRequired(project + ".datastore.protocol");
            String host = config.getRequired(project + ".datastore.host");
            String defaultUser = config.getRequired(project + ".datastore.defaultUser");
            String defaultPassword = config.getRequired(project + ".datastore.defaultPassword");
            return new WebDAVDataStore(protocol, host, defaultUser, defaultPassword); 
            
        }
        
        return null;
        
    }
    
    private static boolean projectConfigured(String project, Config config) {
        
        List<String> projects = config.getRequiredList("projects");
        
        return projects.contains(project);
        
    }
}
