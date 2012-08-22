package no.met.metadataeditor.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import com.sun.jersey.test.framework.JerseyTest;

public class SimplePutValidatorTest extends JerseyTest {
    public SimplePutValidatorTest() {
        super("no.met.metadataeditor");
    }

    @Override
    public int getPort(int defaultPort) {
        return 8888;
    }

    public int getPort() {
        return getPort(8888);
    }

    @Test
    public void testValidateMM2() throws IOException {
        File xmlFile = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(200).when().post("/validator/mm2");
    }
    @Test
    public void testInValidateMM2() throws IOException {
        File xmlFile = new File(RestAPITest.class.getResource("/service/datastore/XML/metadata1wrong.xml").getFile());
        String xml = FileUtils.readFileToString(xmlFile);

        given().port(getPort()).formParam("xml", xml).expect().statusCode(400).when().post("/validator/mm2");
    }
}
