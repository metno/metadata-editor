package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TemplateHandlerTest {

    @Test
    public void testParseTemplate() {
        URL url = getClass().getResource("/mm2TemplateTest.xml");
        Map<String, EditorVariable> mse = null;
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
            mse = et.getTemplate();
            assertNotNull(mse);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditorVariable wmsSetup = mse.get("wmsSetup");
        assertNotNull(wmsSetup);
        assertNotNull(wmsSetup.getChildren().get("firstDisplayLayer"));

        URL xmlUrl = getClass().getResource("/exampleMM2.xml");
        try {
            et.getContent(new InputSource(xmlUrl.openStream()));
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
