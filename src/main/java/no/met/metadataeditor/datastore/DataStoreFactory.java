package no.met.metadataeditor.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.met.metadataeditor.Config;
import no.met.metadataeditor.EditorException;

/**
 * Factory class for creating new DataStore objects.
 */
public class DataStoreFactory {

    private static Config config_ = null;
    private static Map<String, DataStore> store_ = new HashMap<>();

    /**
     * Reread the metadataeditor.properties file and reset all datastores.
     *
     * This function is mainly useful for testing purposes.
     */
    public static synchronized void reset() {
        config_ = null;
        store_.clear();
    }

    private static synchronized Config getConfig() {
        if (config_ == null) {
            config_ = new Config("/metadataeditor.properties", Config.ENV_NAME);
        }
        return config_;
    }

    /**
     * @return A DataStore instance based on the values found in the metadata editor configuration.
     *         The datastore is a singleton.
     */
    public static synchronized DataStore getInstance(String project) {

        Config config = getConfig();

        if(!projectConfigured(project, config)){
            throw new EditorException("Project has not been configured: " + project, EditorException.MISSING_PROJECT_CONFIG );
        }
        return getStore(project, config);

    }

    private static boolean projectConfigured(String project, Config config) {

        List<String> projects = config.getRequiredList("projects");

        return projects.contains(project);
    }

    private static synchronized DataStore getStore(String project, Config config) {
        if (!store_.containsKey(project)) {
            String datastore = config.getRequired(project + ".datastore.type");
            if( "DiskDataStore".equalsIgnoreCase(datastore)){

                String path;
                if( config.get(project + ".datastore.path") != null ){
                    path = config.get(project + ".datastore.path");
                } else {
                    path = System.getProperty("java.io.tmpdir");
                }

                store_.put(project, new DiskDataStore(path));
            } else if ( "WebDAVDataStore".equalsIgnoreCase(datastore)){

                String protocol = config.getRequired(project + ".datastore.protocol");
                String host = config.getRequired(project + ".datastore.host");
                String defaultUser = config.getRequired(project + ".datastore.defaultUser");
                String defaultPassword = config.getRequired(project + ".datastore.defaultPassword");
                store_.put(project, new WebDAVDataStore(protocol, host, defaultUser, defaultPassword));
            }
        }
        return store_.get(project);
    }

}
