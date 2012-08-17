package no.met.metadataeditor.dataTypes.attributes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;


import no.met.metadataeditor.dataTypes.AttributesMismatchException;
import no.met.metadataeditor.dataTypes.DataType;

import org.junit.Test;

public class DataAttributesTest {

    @Test
    public void testGetAttributeExists() {
        
        TestAttributes ta = new TestAttributes();
        String testValue = "test";
        ta.val = testValue;
        
        assertEquals("Get fetches attribute as expected", testValue, ta.getAttribute("val"));
        
        String subTestValue = "10";
        testValue = "other";
        TestAttributesSub tas = new TestAttributesSub();
        tas.val = testValue;
        tas.otherAttribute = subTestValue;
        
        assertEquals("Get fetches attribute from sub class as expected", testValue, tas.getAttribute("val"));        
        assertEquals("Get fetches attribute from super class as expected", subTestValue, tas.getAttribute("otherAttribute"));        
    }
    
    
    @Test(expected=AttributesMismatchException.class)
    public void testGetAttributeNotExposed () {
        
        TestAttributes ta = new TestAttributes();
        ta.getAttribute("notExposed");
        
    }

    @Test(expected=AttributesMismatchException.class)
    public void testGetAttributeDoesNotExist () {
        
        TestAttributes ta = new TestAttributes();
        ta.getAttribute("dsfsfdsfd");
        
    }
    
    @Test
    public void testGetAttributeSetup () {
        
        TestAttributes ta = new TestAttributes();
        Map<String, DataType> setup1 = new HashMap<String, DataType>();
        setup1.put("val", DataType.STRING);
        
        assertEquals("Attribute setup for normal class", setup1, ta.getAttributesSetup());

        TestAttributes tas = new TestAttributesSub();
        Map<String, DataType> setup2 = new HashMap<String, DataType>();
        setup2.put("val", DataType.STRING);
        setup2.put("otherAttribute", DataType.NUMBER);

        assertEquals("Attribute setup for subclass", setup2, tas.getAttributesSetup());        
    }
    
    @Test
    public void testSetAttributeExists() {
        
        TestAttributes ta = new TestAttributes();
        String testValue = "a value";
        ta.addAttribute("val", testValue);

        assertEquals("Get fetches attribute as expected", testValue, ta.val);
        
        String subTestValue = "10";
        testValue = "other";
        TestAttributesSub tas = new TestAttributesSub();
        
        tas.addAttribute("val", testValue);
        tas.addAttribute("otherAttribute", subTestValue);
                
        assertEquals("Get fetches attribute from sub class as expected", testValue, tas.val);        
        assertEquals("Get fetches attribute from super class as expected", subTestValue, tas.otherAttribute);        
    }
    
    
    @Test(expected=AttributesMismatchException.class)
    public void testSetAttributeNotExposed () {
        
        TestAttributes ta = new TestAttributes();
        ta.addAttribute("notExposed", "test");
        
    }

    @Test(expected=AttributesMismatchException.class)
    public void testSetAttributeDoesNotExist () {
        
        TestAttributes ta = new TestAttributes();
        ta.addAttribute("dsfsfdsfd", "test");
        
    }    
    
}
