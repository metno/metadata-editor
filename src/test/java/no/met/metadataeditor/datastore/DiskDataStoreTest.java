package no.met.metadataeditor.datastore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import no.met.metadataeditor.EditorException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static no.met.metadataeditor.TestHelpers.*;
import static org.junit.Assert.*;

public class DiskDataStoreTest {

    private static String baseDir = "DiskDataStoreTest";
    private static File testDir;
    
    @BeforeClass
    public static void setup() throws IOException{
        
        String tmpDir = System.getProperty("java.io.tmpdir");
        testDir = new File(tmpDir, baseDir);
        testDir.mkdirs();
        
        File startDir = new File(DiskDataStoreTest.class.getResource("/datastore/diskdatastore/testProject").getFile());
        
        FileUtils.copyDirectory(startDir, testDir);
    }
    
    @AfterClass
    public static void teardown() throws IOException{
        
        FileUtils.deleteDirectory(testDir);
        
    }
    
    @Test
    public void testExistingTemplate() {
        
        DataStore store = new DiskDataStore(testDir.getAbsolutePath());
        String template = store.readTemplate("metadata1");
        String expectedTemplate = fileAsString("/datastore/diskdatastore/testProject/config/MM2Template.xml");
        assertEquals(expectedTemplate, template);        
        
    }
    
    @Test(expected=EditorException.class)
    public void testNonExistingTemplate() {

        DataStore store = new DiskDataStore(testDir.getAbsolutePath());
        store.readTemplate("mm2combined");
        
        
    }
    
    @Test
    public void testAvailableMetadata(){
    
        DataStore store = new DiskDataStore(testDir.getAbsolutePath());
        List<String> expected = Arrays.asList("metadata1", "mm2combined");
        assertEquals("Available list as expected", expected, store.availableMetadata());
        
    }
    
}
