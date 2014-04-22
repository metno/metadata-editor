package no.met.metadataeditor.dataTypes;

import static no.met.metadataeditor.TestHelpers.getContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTemplateTest {

    @Test
    public void testSingleValueWrite() throws IOException, JDOMException {

        EditorTemplate et = getTemplate("/testWrite/singleVarTemplate.xml");

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/singleValueResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testMultiValueWrite() throws IOException, JDOMException {

        EditorTemplate et = getTemplate("/testWrite/singleVarTemplate.xml");

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("ice"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/multiValueResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testNoContentWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/noContentTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/noContentResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testSiblingWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/siblingTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("ice"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/siblingResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testSubTreeInVarWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/subTreeInVarTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/subTreeInVarResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testSubTreeInVarWrite2() throws IOException, JDOMException {

        String templateName = "/testWrite/subTreeInVarTemplate2.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/subTreeInVarResult2.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testVarInAttributeWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/varInAttributeTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> keywordContent = new ArrayList<>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/varInAttributeResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }


    @Test
    public void testBoundingboxWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/boundingboxTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<>();
        List<EditorVariableContent> localBBContent = new ArrayList<>();
        localBBContent.add(EditorVariableContentFactory.childlessSingleBBAttribute("0,85,-10,54"));
        content.put("localBB", localBBContent);
        List<EditorVariableContent> globalBBContent = new ArrayList<>();
        globalBBContent.add(EditorVariableContentFactory.childlessBoundingboxAttribute("0", "85", "-10", "54"));
        content.put("globalBB", globalBBContent);

        org.jdom2.Document result = et.writeContent(content);

        org.jdom2.Document expected = openDocument("/testWrite/boundingboxResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test
    public void testChildWrite() throws IOException, JDOMException {

        String templateName = "/testWrite/childTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        List<EditorVariableContent> innerContent = new ArrayList<>();
        innerContent.add(EditorVariableContentFactory.childlessStringAttribute("Inner most1"));
        innerContent.add(EditorVariableContentFactory.childlessStringAttribute("Inner most2"));
        Map<String,List<EditorVariableContent>> otherLayersChildren = new HashMap<>();
        otherLayersChildren.put("inner", innerContent);

        List<EditorVariableContent> otherLayersContent = new ArrayList<>();
        otherLayersContent.add(EditorVariableContentFactory.stringAttributeWithChildren("children", otherLayersChildren));
        otherLayersContent.add(EditorVariableContentFactory.childlessStringAttribute("no children"));

        List<EditorVariableContent> firstDisplayLayerContent = new ArrayList<>();
        firstDisplayLayerContent.add(EditorVariableContentFactory.childlessStringAttribute("Display"));

        Map<String, List<EditorVariableContent>> wmsSetupChildren = new HashMap<>();
        wmsSetupChildren.put("otherLayers", otherLayersContent);
        wmsSetupChildren.put("firstDisplayLayer", firstDisplayLayerContent);

        List<EditorVariableContent> wmsSetupContent = new ArrayList<>();
        wmsSetupContent.add(EditorVariableContentFactory.stringAttributeWithChildren("url1", wmsSetupChildren));

        Map<String, List<EditorVariableContent>> varMap = new HashMap<>();
        varMap.put("wmsSetup", wmsSetupContent);

        org.jdom2.Document result = et.writeContent(varMap);

        org.jdom2.Document expected = openDocument("/testWrite/childResult.xml");

        assertEquals(documentToString(expected), documentToString(result));
    }


    @Test
    public void testSingleValueContent() {

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/singleVarTemplate.xml", "/testContent/singleValueContent.xml");

        assertEquals("Content for variable found", true, content.containsKey("keyword"));

        List<EditorVariableContent> evc = content.get("keyword");

        assertEquals("Only a single value for attribute", 1, evc.size());
        assertEquals("Value for content ok", "arctic", evc.get(0).getAttrs().getAttribute("str"));
    }

    @Test
    public void testMultiValueContent() {

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/singleVarTemplate.xml", "/testContent/multiValueContent.xml");

        assertEquals("Content for variable found", true, content.containsKey("keyword"));

        List<EditorVariableContent> evc = content.get("keyword");

        assertEquals("Two values for attribute", 2, evc.size());
        assertEquals("Value for content ok", "arctic", evc.get(0).getAttrs().getAttribute("str"));
        assertEquals("Value for content ok", "sea ice", evc.get(1).getAttrs().getAttribute("str"));
    }

    @Test
    public void testChildContent() {

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/childTemplate.xml", "/testContent/childContent.xml");

        assertEquals("Content for parent found", true, content.containsKey("wmsSetup"));

        List<EditorVariableContent> evc = content.get("wmsSetup");

        assertEquals("Only one content variable", 1, evc.size());

        Map<String, List<EditorVariableContent>> children = evc.get(0).getChildren();

        assertEquals("Child found as expected", true, children.containsKey("displayLayer"));
        assertEquals("Child has expected number of values", 3, children.get("displayLayer").size());

        List<EditorVariableContent> childValues = children.get("displayLayer");
        assertEquals("Value for child ok", "air_temperature", childValues.get(0).getAttrs().getAttribute("str"));
        assertEquals("Value for child ok", "BOXFILL/rainbow", childValues.get(0).getAttrs().getAttribute("listElement"));
        assertEquals("Value for child ok", "percipitation", childValues.get(2).getAttrs().getAttribute("str"));
        assertEquals("Value for child ok", "BOXFILL/rainbow", childValues.get(2).getAttrs().getAttribute("listElement"));

    }

    @Test
    public void testVarInAttributeContent(){

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/varInAttributeTemplate.xml", "/testContent/varInAttributeContent.xml");

        assertEquals("Content for variable found", true, content.containsKey("dates"));

        List<EditorVariableContent> evc = content.get("dates");

        assertEquals("Only a single value for attribute", 1, evc.size());
        assertEquals("Value for content ok", "created", evc.get(0).getAttrs().getAttribute("listElement"));
        assertEquals("Value for content ok", "2012-01-01", evc.get(0).getAttrs().getAttribute("str"));

    }

    @Test
    public void testMultiValueChild(){

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/multiValueChildTemplate.xml", "/testContent/multiValueChildContent.xml");

        assertTrue("Container variable found", content.containsKey("onlineResource"));

        List<EditorVariableContent> evc = content.get("onlineResource");

        assertEquals("Number of sub values found", 3, evc.size());

        Map<String,List<EditorVariableContent>> child1 = evc.get(0).getChildren();

        assertTrue("Child variable found", child1.containsKey("uri"));
        assertEquals("Child has correct number of values", 1, child1.get("uri").size() );

    }

    @Test
    public void testVariableWithHardcodedXPath(){

        Map<String,List<EditorVariableContent>> content = getContent("/testContent/hardcodedXPathTemplate.xml", "/testContent/hardcodedXPathContent.xml");
        assertTrue("Storage units variable found", content.containsKey("storageUnits"));

        List<EditorVariableContent> evc = content.get("storageUnits");
        assertEquals("Number of values found", 2, evc.size());

        assertEquals("Value of first content", "Test", evc.get(0).getAttrs().getAttribute("key"));
        assertEquals("Value of first content", "dummy2", evc.get(1).getAttrs().getAttribute("value"));


    }



    @Test
    public void testFindNamespaces() throws IOException{

        Map<String,String> expected = new HashMap<>();
        expected.put("http://www.met.no/schema/metadataeditor/editorDataTypes", "edt");
        expected.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        expected.put("http://www.isotc211.org/2005/gmd", "gmd");
        expected.put("http://www.isotc211.org/2005/gco", "gco");
        expected.put("http://www.isotc211.org/2005/gmx", "gmx");
        expected.put("http://www.opengis.net/gml", "gml");
        expected.put("http://www.w3.org/1999/xlink", "xlink");

        InputStream input = getClass().getResourceAsStream("/datatypes/findNamespaces1.xml");
        String templateXML = IOUtils.toString(input);

        assertEquals("Namespaces found as expected", expected, EditorTemplate.findNamespaces(templateXML));

    }

    private EditorTemplate getTemplate(String templateResource ){

        URL templateUrl = getClass().getResource(templateResource);
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(templateUrl.openStream()));

        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        return et;
    }

    private String documentToString(org.jdom2.Document doc){

        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        try {
            xout.output(doc, writer);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return writer.toString();
    }

    private org.jdom2.Document openDocument(String filename){

        URL fileUrl = getClass().getResource(filename);
        SAXBuilder builder = new SAXBuilder();
        org.jdom2.Document doc = null;
        try {
            doc = builder.build(new InputSource(fileUrl.openStream()));

        } catch (JDOMException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return doc;

    }


}
