package no.met.metadataeditor.service;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;

import no.met.metadataeditor.TestHelpers;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.test.framework.JerseyTest;

public class SimplePutValidatorTest extends JerseyTest {
    public SimplePutValidatorTest() {
        super("no.met.metadataeditor");
    }
    private static String baseDir = "SimplePutValidatorTest";

    private static File testDir;


    @BeforeClass
    public static void setup() throws IOException {

        String tmpDir = System.getProperty("java.io.tmpdir");
        testDir = new File(tmpDir, baseDir);
        testDir.mkdirs();

        File startDir = new File(SimplePutValidatorTest.class.getResource("/service/datastore/").getFile());
        FileUtils.copyDirectory(startDir, testDir);

        File propertiesFile = new File(testDir, "metadataeditor.properties");
        String properties = "projects=test\n";
        properties += "test.datastore.type=DiskDataStore\n";
        properties += "test.datastore.path=" + testDir + "\n";

        FileUtils.write(propertiesFile, properties);
        TestHelpers.setEditorConfigEnv(propertiesFile.getAbsolutePath());

    }


    @Override
    public int getPort(int defaultPort) {
        return 8889;
    }

    public int getPort() {
        return getPort(8889);
    }

    @Test
    public void testValidateMM2Static() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/mm2");
    }
    @Test
    public void testInValidateMM2Static() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1wrong.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/mm2");
    }

    @Test
    public void testValidateMM2() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/test/mm2");
    }
    @Test
    public void testInValidateMM2() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1wrong.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/test/mm2");
    }

    @Test
    public void testValidateMM2Store() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/test/mm2Store");
        // test repeatedly - caching
        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/test/mm2Store");
        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/test/mm2Store");
    }
    @Test
    public void testInValidateMM2Store() throws IOException {
        File xmlFile = new File(SimplePutValidatorTest.class.getResource("/service/datastore/XML/metadata1wrong.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/test/mm2Store");
        // test repeatedly - caching
        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/test/mm2Store");
        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/test/mm2Store");
    }

}
