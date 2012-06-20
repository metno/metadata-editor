package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TemplateHandlerTest {

    @Test
    public void testParseTemplate() {
        URL url = getClass().getResource("/mm2TemplateTest.xml");
        Map<String, EditorVariable> mse = null;
        try {
            mse = TemplateHandler.parseTemplate(new InputSource(url.openStream()));
            assertNotNull(mse);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(mse.get("wmsSetup"));
    }

}
