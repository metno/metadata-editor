package no.met.metadataeditor;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class ConfigTest {    
    
    @Test
    public void testNormalPropertyFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);      
        assertEquals("Not required property is found", "DiskDataStore", cfg.get("datastore.type"));
    }

    
    @Test
    public void testNormalPropertyNotFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);      
        assertEquals("Missing property return null", null, cfg.get("datastore.tile"));
    }

    @Test
    public void testRequiredPropertyFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);      
        assertEquals("Required property is found", "DiskDataStore", cfg.getRequired("datastore.type"));
    }      
    
    @Test(expected=ConfigException.class)
    public void testRequiredPropertyNotFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);   
        cfg.getRequired("datastore.tile");
    }    

    @Test
    public void testListPropertyFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);
        List<String> expected = Arrays.asList(new String[]{"site1", "site 2", "other site" } );
        assertEquals("List property is found", expected, cfg.getList("datastore.names"));
    } 

    @Test
    public void testListPropertyNotFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);
        
        assertEquals("List property is not found", null, cfg.getList("datastore.passwords"));
    } 
    
    
    @Test
    public void testRequiredListPropertyFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);
        List<String> expected = Arrays.asList(new String[]{"site1", "site 2", "other site" } );
        assertEquals("Required list property is found", expected, cfg.getRequiredList("datastore.names"));
    }     

    @Test(expected=ConfigException.class)
    public void testRequiredListPropertyNotFound()
    {
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME);   
        cfg.getRequiredList("datastore.list");
    }     
    
    @Test
    public void testLocalPropertiesOverride()
    {
        
        Config cfg = new Config("/config/test1.properties", Config.ENV_NAME){
        
               protected Properties getLocalProperties(String resourceNames){
                   Map<String, String> map = new HashMap<>();
                   map.put("datastore.type", "WebDAVDataStore");
                   Properties prop = new Properties();
                   prop.putAll(map);        
                   return prop;
               }
               
               protected String getEnv(String env){
                   return "dummy";
               }
            
        };
        
        
        assertEquals("Local config overrides default", "WebDAVDataStore", cfg.getRequired("datastore.type"));
    }         
    
}
