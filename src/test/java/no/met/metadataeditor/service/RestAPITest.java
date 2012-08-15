package no.met.metadataeditor.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import no.met.metadataeditor.Config;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import com.sun.jersey.test.framework.JerseyTest;

public class RestAPITest extends JerseyTest {

    private static String baseDir = "RestAPITest";

    private static File testDir;

    public RestAPITest() {
        super("no.met.metadataeditor");
    }

    @Override
    public int getPort(int defaultPort) {
        return 8888;
    }
    
    public int getPort(){
        return getPort(0);
    }

    @BeforeClass
    public static void setup() throws IOException {

        String tmpDir = System.getProperty("java.io.tmpdir");
        testDir = new File(tmpDir, baseDir);
        testDir.mkdirs();

        File startDir = new File(RestAPITest.class.getResource("/service/datastore/").getFile());
        FileUtils.copyDirectory(startDir, testDir);

        File propertiesFile = new File(testDir, "metadataeditor.properties");
        String properties = "projects=test\n";
        properties += "test.datastore.type=DiskDataStore\n";
        properties += "test.datastore.path=" + testDir + "\n";

        FileUtils.write(propertiesFile, properties);
        setEditorConfigEnv(propertiesFile.getAbsolutePath());

    }

    @AfterClass
    public static void teardown() throws IOException {
        FileUtils.deleteDirectory(testDir);
    }

    @Test
    public void testGetMetadata() throws IOException {

        File metadata1File = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String metadataXML = FileUtils.readFileToString(metadata1File);

        given().port(getPort()).expect().statusCode(404).when().get("/test/does-not-exist");

        given().port(getPort()).expect().body(equalTo(metadataXML)).statusCode(200).when().get("/test/metadata1");

    }
    
    // posting to non-existant metadata stores the metadata and returns editor url
    
    // POST without metadata to non-existant metadata gives 404
    
    // POST with equal metadata gives editor url
    
    // POST with un-equal metadata gives compare url

    @Test
    public void testPostDoesNotExist(){
        // Sending a POST without metadata to non-existant metadata gives 404
        given().port(getPort()).expect().statusCode(404).when().post("/test/does-not-exist");
    }

    @Test
    public void testPostWithoutMetadata(){
        // Sending a POST without metadata to an existant metadata returns url to editor
        given().port(getPort()).expect().body(containsString("editor.xhtml")).when().post("/test/metadata1");        
    }
    
    @Test
    public void testPostDoesNotExistWithMetadata() throws IOException{
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MM2/>";
        
        // Sending a POST with metadata to a non-existant record creates the record
        given().port(getPort()).formParam("metadata", xml).expect().body(containsString("editor.xhtml")).when().post("/test/new_metadata");
                
        given().port(getPort()).expect().body(equalToIgnoringWhiteSpace(xml)).statusCode(200).when().get("/test/new_metadata");
        
    }
    
    @Test
    public void testPostEqualMetadata() throws IOException {

        File metadata1File = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String metadataXML = FileUtils.readFileToString(metadata1File);        
    
        given().port(getPort()).formParam("metadata", metadataXML).expect().body(containsString("editor.xhtml")).when().post("/test/metadata1");
        
    }
    
    @Test
    public void testPostUnequalMetadata(){
        
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MM2/>";
        given().port(getPort()).formParam("metadata", xml).expect().body(containsString("compare.xhtml")).when().post("/test/metadata1");
        
    }
    
    
    private static void setEditorConfigEnv(String editorConfigPath) {

        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> env = System.getenv();
        for (Class cl : classes) {
            if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                Field field;
                try {
                    field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.put(Config.ENV_NAME, editorConfigPath);

                } catch (NoSuchFieldException e) {
                    fail(e.getMessage());
                } catch (SecurityException e) {
                    fail(e.getMessage());
                } catch (IllegalArgumentException e) {
                    fail(e.getMessage());
                } catch (IllegalAccessException e) {
                    fail(e.getMessage());
                }
            }
        }

    }
}
