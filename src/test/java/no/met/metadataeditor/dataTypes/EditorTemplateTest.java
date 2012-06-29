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

}
