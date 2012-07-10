package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTemplateTest {

    @Test 
    public void testSingleValueWrite() throws IOException, JDOMException {
        
        EditorTemplate et = getTemplate("/testWrite/singleVarTemplate.xml");
        
        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        content.put("keyword", keywordContent);
        
        URL metadataUrl = getClass().getResource("/testWrite/singleVarTemplate.xml");               
        org.jdom2.Document result = et.writeContent(new InputSource(metadataUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/singleValueResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }

    @Test 
    public void testMultiValueWrite() throws IOException, JDOMException {
        
        EditorTemplate et = getTemplate("/testWrite/singleVarTemplate.xml");
        
        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("ice"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));
        content.put("keyword", keywordContent);
        
        URL metadataUrl = getClass().getResource("/testWrite/singleVarTemplate.xml");               
        org.jdom2.Document result = et.writeContent(new InputSource(metadataUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/multiValueResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }    
    
    @Test 
    public void testNoContentWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/noContentTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        content.put("keyword", keywordContent);

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/noContentResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }       

    @Test 
    public void testSiblingWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/siblingTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("ice"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));        
        content.put("keyword", keywordContent);

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/siblingResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }    
    
    @Test 
    public void testSubTreeInVarWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/subTreeInVarTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));        
        content.put("keyword", keywordContent);

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/subTreeInVarResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }      
    
    @Test 
    public void testSubTreeInVarWrite2() throws IOException, JDOMException {
        
        String templateName = "/testWrite/subTreeInVarTemplate2.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("arctic"));
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));        
        content.put("keyword", keywordContent);

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/subTreeInVarResult2.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }     
    
    @Test 
    public void testVarInAttributeWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/varInAttributeTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> keywordContent = new ArrayList<EditorVariableContent>();
        keywordContent.add(EditorVariableContentFactory.childlessStringAttribute("snow"));        
        content.put("keyword", keywordContent);

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/varInAttributeResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }        
    
    
    @Test 
    public void testBoundingboxWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/boundingboxTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        Map<String, List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        List<EditorVariableContent> localBBContent = new ArrayList<EditorVariableContent>();
        localBBContent.add(EditorVariableContentFactory.childlessSingleBBAttribute("0,85,-10,54"));        
        content.put("localBB", localBBContent);
        List<EditorVariableContent> globalBBContent = new ArrayList<EditorVariableContent>();
        globalBBContent.add(EditorVariableContentFactory.childlessBoundingboxAttribute("0", "85", "-10", "54"));        
        content.put("globalBB", globalBBContent);
        

        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), content);
        
        org.jdom2.Document expected = openDocument("/testWrite/boundingboxResult.xml");
        
        assertEquals(documentToString(expected), documentToString(result));
    }         
    
    @Test 
    public void testChildWrite() throws IOException, JDOMException {
        
        String templateName = "/testWrite/childTemplate.xml";
        EditorTemplate et = getTemplate(templateName);

        List<EditorVariableContent> innerContent = new ArrayList<EditorVariableContent>();
        innerContent.add(EditorVariableContentFactory.childlessStringAttribute("Inner most1"));
        innerContent.add(EditorVariableContentFactory.childlessStringAttribute("Inner most2"));
        Map<String,List<EditorVariableContent>> otherLayersChildren = new HashMap<String,List<EditorVariableContent>>();
        otherLayersChildren.put("inner", innerContent);
        
        List<EditorVariableContent> otherLayersContent = new ArrayList<EditorVariableContent>();
        otherLayersContent.add(EditorVariableContentFactory.stringAttributeWithChildren("children", otherLayersChildren));
        otherLayersContent.add(EditorVariableContentFactory.childlessStringAttribute("no children"));
        
        List<EditorVariableContent> firstDisplayLayerContent = new ArrayList<EditorVariableContent>();
        firstDisplayLayerContent.add(EditorVariableContentFactory.childlessStringAttribute("Display"));
        
        Map<String, List<EditorVariableContent>> wmsSetupChildren = new HashMap<String,List<EditorVariableContent>>();
        wmsSetupChildren.put("otherLayers", otherLayersContent);
        wmsSetupChildren.put("firstDisplayLayer", firstDisplayLayerContent);

        List<EditorVariableContent> wmsSetupContent = new ArrayList<EditorVariableContent>();
        wmsSetupContent.add(EditorVariableContentFactory.stringAttributeWithChildren("url1", wmsSetupChildren));
        
        Map<String, List<EditorVariableContent>> varMap = new HashMap<String,List<EditorVariableContent>>();
        varMap.put("wmsSetup", wmsSetupContent);
        
        URL templateUrl = getClass().getResource(templateName);               
        org.jdom2.Document result = et.writeContent(new InputSource(templateUrl.openStream()), varMap);
        
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
    public void testVariableNames() {

        Map<String, EditorVariable> mse = getVariables("/mm2TemplateTest.xml");

        List<String> expectedVarNames = Arrays.asList(new String[] { "wmsSetup", "globalBB", "localBB", "variableList",
                "PIname", "timeExtend", "dataRef" });

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
       
    private Map<String, EditorVariable> getVariables(String templateResource ){
        
        URL url = getClass().getResource(templateResource);
        Map<String, EditorVariable> mse = null;
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
            mse = et.getTemplate();
            assertNotNull(mse);
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }        
        return mse;
    }

    private Map<String, List<EditorVariableContent>> getContent(String templateResource, String metadataResource ){
        
        URL templateUrl = getClass().getResource(templateResource);
        URL metadataUrl = getClass().getResource(metadataResource);
        EditorTemplate et = null;
        Map<String, List<EditorVariableContent>> content = null;
        try {
            et = new EditorTemplate(new InputSource(templateUrl.openStream()));
            content = et.getContent(new InputSource(metadataUrl.openStream()));
            
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            fail();
        }
        return content;        
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
