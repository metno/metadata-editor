package no.met.metadataeditor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility Class for reading configuration variables.
 *
 * The configuration is expected to be a Java properties file.
 */
public class Config {

    public static final String ENV_NAME = "METADATA_EDITOR_PROPERTIES";
    
    private String environmentVariable;
    private Properties config;
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    /**
     * Constructor to create object with resource name and local environment variable
     *
     * @param propertyResource The name of the properties file including the path
     * @param environmentVariable The name of the environment variable for local properties files
     */
    public Config(String propertyResource, String env) {
        try {
            config = getDefaultproperties(propertyResource);
            this.environmentVariable = env;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to open config: ", e);
            throw new ConfigException("Failed to read config file", e);
        }
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public String getEnv() {
        return environmentVariable;
    }

    public void setEnv(String env) {
        this.environmentVariable = env;
    }

    private Properties getDefaultproperties(String resourceName) throws IOException {
        Properties prop = new Properties();
        prop.load(Config.class.getResourceAsStream(resourceName));
        return prop;
    }

    protected Properties getLocalProperties(String resourceName) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(resourceName));
        return prop;
    }

    /**
     * Get a required configuration variable from the configuration. Throw a runtime exception if it does not exist.
     *
     * @param key The name of the configuration variable.
     * @return The value of the configuration variable.
     */
    public String getRequired(String key) {

        String value = get(key);
        if (value == null) {
            throw new ConfigException("The config variable '" + key + "' is required but not set.");
        }

        return value;
    }
    
    public String get(String key){
        
        Collection<Object> commonProps = new HashSet<Object>();
        Properties local = null;
        if (environmentVariable != null) {
            try {
                String env = getEnv(environmentVariable);
                if (env.length() > 0) {
                    local = getLocalProperties(env);
                    Set<Object> localKey = local.keySet();
                    Set<Object> configKey = config.keySet();
                    commonProps = getCommonProperties(configKey, localKey);
                }
            } catch (NullPointerException npex) {
                //logger.severe(npex.getMessage());
                logger.log(Level.INFO, "Failed to get ENV '" + environmentVariable + "'", npex.getMessage());
            } catch (IOException ioex) {
                logger.log(Level.SEVERE, "Failed to open config:", ioex.getMessage());
                //throw new ConfigException("Failed to read config file", ioex);
            }
        }

        String value = null;
        if (commonProps.size() > 0 && commonProps.contains(key)) {
            value = local.getProperty(key);
        } else {
            value = config.getProperty(key);
        }
        return value;
    }

    /**
     * Get a required configuration item from the configuration file. Throw an exception if does not exist.
     *
     * This function supports turning comma separated values into a list of values.
     * @param key The configuration key
     * @return A list of value that is created by splitting a comma-separated configuration value.
     */
    public List<String> getRequiredList(String key){

        String listValue = getRequired(key);

        return splitListString(listValue);
    }

    /**
     * Get a configuration item from the configuration file.
     *
     * This function supports turning comma separated values into a list of values.
     * @param key The configuration key
     * @return A list of value that is created by splitting a comma-separated configuration value. Null if the configuration item does not exist.
     */
    public List<String> getList(String key){

        String listValue = get(key);

        if(listValue == null){
            return null;
        }
        
        return splitListString(listValue);
    }    
    
    private List<String> splitListString(String stringList){

        String[] listValues = stringList.split(",");
        for( int i = 0; i < listValues.length; i++){
            listValues[i] = listValues[i].trim();
        }
        return Arrays.asList(listValues);
        
    }
    
    protected String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null) {
            throw new NullPointerException("The ENV variable '" + key + "' is required but not set.");
        }
        return value;
    }

    private Collection<Object> getCommonProperties(Collection<Object> c1, Collection<Object> c2) {
        Collection<Object> result = new HashSet<Object>(c1);
        result.retainAll(c2);
        return result;
    }
}
