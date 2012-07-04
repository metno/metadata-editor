package no.met.metadataeditor.datastore;

import no.met.metadataeditor.EditorException;

import org.junit.BeforeClass;
import org.junit.Test;

import static no.met.metadataeditor.TestHelpers.*;
import static org.junit.Assert.*;

public class DataStoreDiskTest {

    @BeforeClass
    public static void setup(){
        
        
        
        
        
    }
    
    @Test
    public void testExistingTemplate() {

        String dirName = getClass().getResource("/datastore/diskdatastore/").getFile();
        DataStore store = new DiskDataStore(dirName);
        String template = store.readTemplate("testProject", "metadata1");
        String expectedTemplate = fileAsString("/datastore/diskdatastore/config/MM2Template.xml");
        assertEquals(expectedTemplate, template);        
        
    }
    
    @Test(expected=EditorException.class)
    public void testNonExistingTemplate() {

        String dirName = getClass().getResource("/datastore/diskdatastore/").getFile();
        DataStore store = new DiskDataStore(dirName);
        store.readTemplate("testProject", "mm2combined");
        
        
    }
    
}
