package no.met.metadataeditor.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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

    private static final String WEBDAV_USERNAME = "dummy";

    private static final String WEBDAV_PASSWORD = "xxx";


    @BeforeClass
    public static void setupWebDAV () throws IOException {

        webdavConn = SardineFactory.begin(WEBDAV_USERNAME, WEBDAV_PASSWORD);

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

        InputStream setup = DataStoreUtilsTest.class.getResourceAsStream("/datastore/diskdatastore/testProject/config/setup.xml");

        webdavConn.put(webdavPath("project1", "config", "setup.xml"), setup);

        webdavConn.put(webdavPath("project1", "config", "MM2Editor.xml"), "Editor config".getBytes() );
        webdavConn.put(webdavPath("project1", "config", "MM2Template.xml"), "Template contents".getBytes() );

        webdavConn.put(webdavPath("project1", "resource1.txt"), "Resource contents".getBytes() );
    }

    @AfterClass
    public static void teardownWebDAV() throws IOException {

        webdavConn.delete( webdavPath("project1"));

    }


    @Test
    public void testMetadataExists() {

        WebDAVDataStore datastore = getDataStore();

        assertFalse("Non-existant record in non-existant project does not exist", datastore.metadataExists("test") );

        assertFalse("Non-existant record in existant project", datastore.metadataExists("dummy"));

        assertTrue("Existant record in existant project", datastore.metadataExists("record1"));

    }

    @Test
    public void testSupportedFormatsExist() {
        WebDAVDataStore datastore = getDataStore();
        List<SupportedFormat> formats = datastore.getSupportedFormats();
        assertEquals(4, formats.size());
    }

    @Test(expected=EditorException.class)
    public void testReadMetadataNoRecord(){

        WebDAVDataStore datastore = getDataStore();
        datastore.readMetadata("dummy");
    }

    @Test
    public void testReadMetadata() {

        WebDAVDataStore datastore = getDataStore();

        assertEquals("Metadata content as expected", "<metadata />", datastore.readMetadata("record1"));

    }

    @Test(expected=EditorException.class)
    public void testReadTemplateNoRecord() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate("dummy");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadTemplateUnsupportedFormat() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate("record1");
    }

    @Test(expected=EditorException.class)
    public void testReadTemplateNoTemplate() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate("iso1");
    }

    @Test
    public void testReadTemplate() {

        WebDAVDataStore datastore = getDataStore();
        assertEquals("Template for MM2 file", "Template contents", datastore.readTemplate("mm2_1"));
    }


    @Test(expected=IllegalArgumentException.class)
    public void testReadTemplateForFormatUnsupportedFormat() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate(new SupportedFormat("dummy", "", "", null));
    }

    @Test(expected=EditorException.class)
    public void testReadTemplateForFormatNoTemplate() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate(new SupportedFormat("MM2COMBINED", "mmCombinedMM2", "http://www.met.no/schema/metamod/mmCombined", null));
    }

    @Test
    public void testReadTemplateForFormat() {

        WebDAVDataStore datastore = getDataStore();
        assertEquals("Template for MM2 file", "Template contents", datastore.readTemplate(new SupportedFormat("MM2", "MM2", "http://www.met.no/schema/metamod/MM2", null)));
    }


    @Test(expected=EditorException.class)
    public void testReadConfigurationNoRecord() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readEditorConfiguration("dummy");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadConfigurationUnsupportedFormat() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readTemplate("record1");
    }

    @Test(expected=EditorException.class)
    public void testReadConfigurationNoTemplate() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readEditorConfiguration("iso1");
    }

    @Test
    public void testReadConfiguration() {

        WebDAVDataStore datastore = getDataStore();
        assertEquals("Editor configuration for MM2 file", "Editor config", datastore.readEditorConfiguration("mm2_1"));
    }


    @Test(expected=EditorException.class)
    public void testReadNonExistantResource() {

        WebDAVDataStore datastore = getDataStore();
        datastore.readResource("dummyResource");
    }

    @Test
    public void testReadResource() {

        WebDAVDataStore datastore = getDataStore();
        assertEquals("Resource content", "Resource contents", datastore.readResource("resource1.txt"));
    }


    @Test
    public void testWriteMetadata(){

        WebDAVDataStore datastore = getDataStore();

        datastore.writeMetadata("write1", "Some xml", WEBDAV_USERNAME, WEBDAV_PASSWORD);

        assertEquals("Data read is same as written", "Some xml", datastore.readMetadata("write1"));

    }

    @Test
    public void testUserHasWriteAccess(){

        WebDAVDataStore datastore = getDataStore();

        assertFalse("Unknown user does not have write access", datastore.userHasWriteAccess("unknown", "dummy"));

        assertFalse("Wrong password does not give write access", datastore.userHasWriteAccess(WEBDAV_USERNAME, "asdfadsfasfdfsa"));

        assertTrue("Correct username and password gives write access", datastore.userHasWriteAccess(WEBDAV_USERNAME, WEBDAV_PASSWORD));

    }

    @Test
    public void testAvailableMetadata(){

        WebDAVDataStore datastore = getDataStore();

        List<String> expected = Arrays.asList("iso1", "mm2_1", "record1", "write1" );
        assertEquals("Available metadata found as expected", expected, datastore.availableMetadata());

    }

    @Test
    public void testDeleteMetadata(){

        WebDAVDataStore datastore = getDataStore();

        String id = "a-new-record-to-delete";
        datastore.writeMetadata(id, "<MM2/>", WEBDAV_USERNAME, WEBDAV_PASSWORD);

        assertTrue("New metadata record created", datastore.metadataExists(id));

        assertTrue("Delete returns true on delete", datastore.deleteMetadata(id, WEBDAV_USERNAME, WEBDAV_PASSWORD));

        assertFalse("New metadata record deleted", datastore.metadataExists(id));

    }

    private static String webdavPath(String... paths){

        File fullPath = new File(webdavHost);

        for( int i = 0; i < paths.length; i++ ){
            fullPath = new File(fullPath, paths[i]);
        }
        return webdavProtocol + "://" + fullPath.toString();

    }

    private WebDAVDataStore getDataStore(){
        return new WebDAVDataStore(webdavProtocol, webdavHost + "/project1", WEBDAV_USERNAME, WEBDAV_PASSWORD);
    }

}
