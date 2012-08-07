package no.met.metadataeditor.datastore;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import no.met.metadataeditor.Config;
import no.met.metadataeditor.EditorException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

/**
 * These test depend on a WebDAV server being available and configured in the 
 * properties file.
 *  
 * @author oysteint
 *
 */
public class WebDAVDataStoreTest {

    private static Sardine webdavConn;
    
    private static String webdavHost = "dev-vm087/svn/unittest";
    
    private static String webdavProtocol = "http";
            
    @BeforeClass
    public static void setupWebDAV () throws IOException {
        
        webdavConn = SardineFactory.begin();
                
        // create the structure in the WebDAV required for the tests.        
        webdavConn.createDirectory( webdavPath("project1" ) );
        
        webdavConn.createDirectory(webdavPath("project1", "config"));
        webdavConn.createDirectory(webdavPath("project1", "XML"));
        
        webdavConn.put(webdavPath("project1", "XML", "record1.xml"), "<metadata />".getBytes() );

        String isoMetadata = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        isoMetadata += "<gmd:MD_Metadata xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" ";
        isoMetadata += "xmlns:dif=\"http://gcmd.gsfc.nasa.gov/Aboutus/xml/dif/\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" ";
        isoMetadata += "xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd http://www.isotc211.org/2005/gmd/gmd.xsd\">";
        isoMetadata += "</gmd:MD_Metadata>";
        webdavConn.put(webdavPath("project1", "XML", "iso1.xml"), isoMetadata.getBytes() );
        
        String mm2Metadata = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        mm2Metadata += "<MM2 xmlns=\"http://www.met.no/schema/metamod/MM2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ";
        mm2Metadata += "xsi:schemaLocation=\"http://www.met.no/schema/metamod/MM2 https://wiki.met.no/_media/metamod/mm2.xsd\">";
        mm2Metadata += "</MM2>";
                
        webdavConn.put(webdavPath("project1", "XML", "mm2_1.xml"), mm2Metadata.getBytes() );     
        
        webdavConn.put(webdavPath("project1", "config", "MM2Editor.xml"), "Editor config".getBytes() );
        webdavConn.put(webdavPath("project1", "config", "MM2Template.xml"), "Template contents".getBytes() );
        
        webdavConn.put(webdavPath("project1", "resource1.txt"), "Resource contents".getBytes() );
    }
    
    @AfterClass
    public static void teardownWebDAV() throws IOException {
        
        webdavConn.delete( webdavPath("project1"));
        
    }
    
    
    @Test
    public void testProjectExists() {
        
        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        
        assertFalse("Non-existant project does not exist", datastore.projectExists("dummy") );
        
        assertTrue("Existant project is found", datastore.projectExists("project1"));
        
    }
    
    @Test
    public void testMetadataExists() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        
        assertFalse("Non-existant record in non-existant project does not exist", datastore.metadataExists("dummy", "test") );
        
        assertFalse("Non-existant record in existant project", datastore.metadataExists("project1", "dummy"));
        
        assertTrue("Existant record in existant project", datastore.metadataExists("project1", "record1"));
        
    }
    
    @Test(expected=EditorException.class)
    public void testReadMetadataNoProject(){

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readMetadata("dummy", "test");
    }

    @Test(expected=EditorException.class)
    public void testReadMetadataNoRecord(){

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readMetadata("project1", "dummy");
    }

    @Test 
    public void testReadMetadata() {
        
        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        
        assertEquals("Metadata content as expected", "<metadata />", datastore.readMetadata("project1", "record1"));
        
    }
    
    @Test(expected=EditorException.class)
    public void testReadTemplateNoRecord() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readTemplate("project1", "dummy");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadTemplateUnsupportedFormat() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readTemplate("project1", "record1");
    }

    @Test(expected=EditorException.class)
    public void testReadTemplateNoTemplate() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readTemplate("project1", "iso1");
    }

    @Test
    public void testReadTemplate() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        assertEquals("Template for MM2 file", "Template contents", datastore.readTemplate("project1", "mm2_1"));
    }    
    
    
    @Test(expected=EditorException.class)
    public void testReadConfigurationNoRecord() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readEditorConfiguration("project1", "dummy");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadConfigurationUnsupportedFormat() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readTemplate("project1", "record1");
    }

    @Test(expected=EditorException.class)
    public void testReadConfigurationNoTemplate() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readEditorConfiguration("project1", "iso1");
    }

    @Test
    public void testReadConfiguration() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        assertEquals("Editor configuration for MM2 file", "Editor config", datastore.readEditorConfiguration("project1", "mm2_1"));
    }       
    
    
    @Test(expected=EditorException.class)
    public void testReadNonExistantResource() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        datastore.readResource("project1", "dummyResource");
    }

    @Test
    public void testReadResource() {

        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        assertEquals("Resource content", "Resource contents", datastore.readResource("project1", "resource1.txt"));
    }      
    
    
    @Test
    public void testWriteMetadata(){
        
        WebDAVDataStore datastore = new WebDAVDataStore(webdavProtocol, webdavHost);
        
        datastore.writeMetadata("project1", "write1", "Some xml");
        
        assertEquals("Data read is same as written", "Some xml", datastore.readMetadata("project1", "write1"));
        
    }
    
    private static String webdavPath(String... paths){
        
        File fullPath = new File(webdavHost);
        
        for( int i = 0; i < paths.length; i++ ){
            fullPath = new File(fullPath, paths[i]);
        }
        return webdavProtocol + "://" + fullPath.toString();
        
    }

}