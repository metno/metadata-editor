package no.met.metadataeditor.service;

import static com.jayway.restassured.RestAssured.*;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.io.File;
import java.io.IOException;
import no.met.metadataeditor.TestHelpers;
import no.met.metadataeditor.validationclient.SimplePutValidationClient;
import no.met.metadataeditor.validationclient.ValidationResponse;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestAPITest extends JerseyTest {

    private static final String baseDir = "RestAPITest";

    private static File testDir;

    public RestAPITest() { 
         super(new WebAppDescriptor.Builder("no.met.metadataeditor")
                .initParam(JSONConfiguration.FEATURE_POJO_MAPPING, "true").build());
        
        SimplePutValidationClient spvc = mock(SimplePutValidationClient.class);        
        when(spvc.validate(anyString())).thenReturn(new ValidationResponse(true, "All good"));
        
        SimplePutValidationClient.setDefaultInstance(spvc);
        
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
        TestHelpers.setEditorConfigEnv(propertiesFile.getAbsolutePath());

    }

    @AfterClass
    public static void teardown() throws IOException {
       FileUtils.deleteDirectory(testDir);
    }

    @Test
    public void testGetMetadata() throws IOException {

        File metadata1File = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String metadataXML = FileUtils.readFileToString(metadata1File);

        given().port(getPort()).expect().statusCode(404).when().get("/metaedit_api/test/does-not-exist");

        given().port(getPort()).expect().body(equalTo(metadataXML)).statusCode(200).when().get("/metaedit_api/test/metadata1");

    }

    @Test
    public void testPostDoesNotExist(){
        // Sending a POST without metadata to non-existant metadata gives 404
        given().port(getPort()).expect().statusCode(404).when().post("/metaedit_api/test/does-not-exist");
    }

    @Test
    public void testPostWithoutMetadata(){
        // Sending a POST without metadata to an existant metadata returns url to editor
        given().port(getPort()).expect().body(containsString("editor.xhtml")).when().post("/metaedit_api/test/metadata1");
    }

    @Test
    public void testPostDoesNotExistWithMetadata() throws IOException{

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MM2 xmlns=\"http://www.met.no/schema/metamod/MM2\"/>";

        // Sending a POST with metadata to a non-existant record creates the record
        given().port(getPort()).body(xml).expect().body(containsString("editor.xhtml")).when().post("/metaedit_api/test/new_metadata");

        given().port(getPort()).expect().body(equalToIgnoringWhiteSpace(xml)).statusCode(200).when().get("/metaedit_api/test/new_metadata");

    }
    
    @Test
    public void testPostWithMetadataNoEdit() throws Exception {
        File metadata1File = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String metadataXML = FileUtils.readFileToString(metadata1File);
        given().port(getPort()).body(metadataXML).expect().statusCode(200).when()
                .post("/metaedit_api/noedit/test/metadata1");
    }
    
    @Test
    public void testListMetadataRecords() throws Exception {
        given().
                port(getPort()).
                header("accept", "application/json").
        expect().
                body("resource.name", 
                        Matchers.hasItems(new String[]{"mm2combined.xml", 
                             "metadata1wrong.xml", "metadata1.xml"})).
                statusCode(200).
        when().
                get("/metaedit_api/list/test/");
    }

    @Test
    public void testPostEqualMetadata() throws IOException {
        File metadata1File = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String metadataXML = FileUtils.readFileToString(metadata1File);

        given().port(getPort()).body(metadataXML).expect().body(containsString("editor.xhtml")).when().post("/metaedit_api/test/metadata1");
    }

    @Test
    public void testPostUnequalMetadata(){

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MM2 xmlns=\"http://www.met.no/schema/metamod/MM2\"/>";
        given().port(getPort()).body(xml).expect().body(containsString("compare.xhtml")).when().post("/metaedit_api/test/metadata1");

    }

}
