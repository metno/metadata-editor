package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTemplateTest {

    @Test
    public void testTemplateParsing() {

        URL url = getClass().getResource("/mm2TemplateTest.xml");
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

        List<String> expectedVarNames = Arrays.asList(new String[] { "wmsSetup", "globalBB", "localBB", "variableList",
                "PIname", "timeExtend", "dataRef" });

        assertEquals("Number of variables in template as expected", expectedVarNames.size(), mse.size());

        for (String varName : expectedVarNames) {
            assertEquals("Variable call '" + varName + "' found", true, mse.containsKey(varName));
        }
        
        EditorVariable wmsSetup = mse.get("wmsSetup");
        
        assertEquals("Explicit min occurs", 0, wmsSetup.getMinOccurs());
        assertEquals("Explicit max occurs", 1, wmsSetup.getMaxOccurs());
        
        Map<String,EditorVariable> wmsSetupChildren = wmsSetup.getChildren();
        assertEquals("Correct number of children for wmsSetup", 2, wmsSetupChildren.size());
                
        assertEquals("wmsSetup has firstDisplayLayer as child", true, wmsSetupChildren.containsKey("firstDisplayLayer"));
        
        EditorVariable firstDisplayLayer = wmsSetupChildren.get("firstDisplayLayer");
        assertEquals("Explicit min occurs in child", 1, firstDisplayLayer.getMinOccurs());
        assertEquals("Explicit max occurs in child", 1, firstDisplayLayer.getMaxOccurs());
        
        EditorVariable localBB = mse.get("localBB");
        assertEquals("unbounded gives expected value", Integer.MAX_VALUE, localBB.getMaxOccurs());
        
        EditorVariable variableList = mse.get("variableList");
        try {
            assertEquals("resource added correctly", variableList.getResources().get(EditorVariable.DEFAULT_RESOURCE), new URI("keywords.txt"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail();
        }
        
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

}
