package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import no.met.metadataeditor.TestHelpers;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class TemplateHandlerTest {

    @Test
    public void testParseTemplate() {
        URL url = getClass().getResource("/mm2TemplateTest.xml");
        Map<String, EditorVariable> mse = null;
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
            mse = et.getVarMap();
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
    
    @Test
    public void testVariableNames() {

        Map<String, EditorVariable> mse = getVariables("/mm2TemplateTest.xml");

        List<String> expectedVarNames = Arrays.asList(new String[] { "wmsSetup", "globalBB", "localBB", "variableList",
                "PIname", "timeExtendTo", "timeExtendFrom", "dataRef" });

        assertEquals("Number of variables in template as expected", expectedVarNames.size(), mse.size());

        for (String varName : expectedVarNames) {
            assertEquals("Variable call '" + varName + "' found", true, mse.containsKey(varName));
        }

    }

    @Test
    public void testChildren() {

        Map<String, EditorVariable> mse = getVariables("/testChildren/basic.xml");

        EditorVariable parent = mse.get("parent");
        Map<String,EditorVariable> children = parent.getChildren();

        assertEquals("Correct number of children", 2, children.size());
        assertEquals("Name of first child", true, children.containsKey("child1"));
        assertEquals("Name of second child", true, children.containsKey("child2"));

        EditorVariable child2 = children.get("child2");
        Map<String,EditorVariable> grandChildren = child2.getChildren();

        assertEquals("Correct number of children", 1, grandChildren.size());
        assertEquals("Name of grandchild", true, grandChildren.containsKey("grandchild"));

    }

    @Test
    public void testValidMinOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMinOccurs/valid.xml");
        EditorVariable mo = mse.get("minOccurs");
        assertEquals(0, mo.getMinOccurs());
    }

    @Test
    public void testLargerMinOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMinOccurs/larger.xml");
        EditorVariable mo = mse.get("minOccurs");
        assertEquals(3, mo.getMinOccurs());
    }

    @Test
    public void testDefaultMinOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMinOccurs/default.xml");
        EditorVariable mo = mse.get("minOccurs");
        assertEquals(1, mo.getMinOccurs());
    }

    @Test(expected=InvalidTemplateException.class)
    public void testEmptyMinOccurs() {
        getVariables("/testMinOccurs/empty.xml");
    }

    @Test(expected=InvalidTemplateException.class)
    public void testNegativeMinOccurs() {
        getVariables("/testMinOccurs/negative.xml");
    }

    @Test(expected=InvalidTemplateException.class)
    public void testUnboundedMinOccurs() {
       getVariables("/testMinOccurs/unbounded.xml");
    }

    @Test
    public void testDefaultMaxOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMaxOccurs/default.xml");
        EditorVariable mo = mse.get("maxOccurs");
        assertEquals(1, mo.getMaxOccurs());
    }

    @Test
    public void testValidMaxOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMaxOccurs/valid.xml");
        EditorVariable mo = mse.get("maxOccurs");
        assertEquals(5, mo.getMaxOccurs());
    }

    @Test
    public void testUnboundedMaxOccurs() {
        Map<String,EditorVariable> mse = getVariables("/testMaxOccurs/unbounded.xml");
        EditorVariable mo = mse.get("maxOccurs");
        assertEquals(Integer.MAX_VALUE, mo.getMaxOccurs());
    }

    @Test(expected=InvalidTemplateException.class)
    public void testZeroMaxOccurs() {
       getVariables("/testMaxOccurs/zero.xml");
    }

    @Test(expected=InvalidTemplateException.class)
    public void testNegativeMaxOccurs() {
       getVariables("/testMaxOccurs/negative.xml");
    }

    @Test(expected=InvalidTemplateException.class)
    public void testStringMaxOccurs() {
       getVariables("/testMaxOccurs/string.xml");
    }

    @Test(expected=InvalidTemplateException.class)
    public void testEmptyMaxOccurs() {
       getVariables("/testMaxOccurs/empty.xml");
    }
    
    

    private static Map<String, EditorVariable> getVariables(String templateResource ){

        String templateXML = TestHelpers.fileAsString(templateResource);

        Map<String, EditorVariable> mse = null;
        TemplateHandler th = new TemplateHandler();
        th.setNamespacePrefixes(EditorTemplate.findNamespaces(templateXML));
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(th);
            xmlReader.parse(new InputSource(new StringReader(templateXML)));
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
            fail();
        }            
        mse = th.getResultConfig();
        return mse;
    }
    

}
