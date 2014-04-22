package no.met.metadataeditor.dataTypes.attributes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.dataTypes.AttributesMismatchException;
import no.met.metadataeditor.dataTypes.DataAttributeValidationResult;
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
        Map<String, DataType> setup1 = new HashMap<>();
        setup1.put("val", DataType.STRING);

        assertEquals("Attribute setup for normal class", setup1, ta.getAttributesSetup());

        TestAttributes tas = new TestAttributesSub();
        Map<String, DataType> setup2 = new HashMap<>();
        setup2.put("val", DataType.STRING);
        setup2.put("otherAttribute", DataType.FLOAT);

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


    @Test
    public void testGetInstanceTestAttributes(){

        TestAttributes ta = new TestAttributes();
        assertTrue("TestAttributes newInstance of correct type", ta.newInstance() instanceof TestAttributes );
    }

    @Test
    public void testGetInstanceTestAttributesSub(){

        TestAttributesSub tas = new TestAttributesSub();
        assertTrue("TestAttributesSub newInstance of correct type", tas.newInstance() instanceof TestAttributesSub );
    }

    @Test
    public void testValidateAttributesAllValid(){

        StringAttribute attr = new StringAttribute();
        attr.addAttribute("str", "test");

        Map<String, DataAttributeValidationResult> expected = new HashMap<>();
        expected.put("str", new DataAttributeValidationResult(true, null));

        assertEquals("All attribute values are valid", expected, attr.validateAttributes());

    }

    @Test
    public void testValidateAttributesSomeValid() {

        LatLonBBAttribute attr = new LatLonBBAttribute();
        attr.addAttribute("north", "70");
        attr.addAttribute("south", "50");
        attr.addAttribute("west", "value");
        attr.addAttribute("east", "");

        Map<String, DataAttributeValidationResult> expected = new HashMap<>();
        expected.put("north", new DataAttributeValidationResult(true, null));
        expected.put("south", new DataAttributeValidationResult(true, null));
        expected.put("west", new DataAttributeValidationResult(false, "'value' is not a valid number."));
        expected.put("east", new DataAttributeValidationResult(false, "'' is not a valid number."));

        assertEquals("Some attribute values are valid", expected, attr.validateAttributes());
    }
    
    @Test
    public void testEquals(){
        
        StringAttribute sa1 = new StringAttribute();
        sa1.addAttribute("str", "test");
        
        StringAttribute sa2 = new StringAttribute();
        sa2.addAttribute("str", "test");
        
        StringAttribute sa3 = new StringAttribute();
        sa3.addAttribute("str", "");
        
        StringAttribute sa4 = new StringAttribute();
        sa4.addAttribute("str", "");

        DateAttribute da1 = new DateAttribute();
        da1.addAttribute("date", "2012-12-12");

        DateAttribute da2 = new DateAttribute();
        da2.addAttribute("date", "2012-12-12");
        
        DateAttribute da3 = new DateAttribute();
        da3.addAttribute("date", "2012-13-12");
        
        assertThat(sa1, equalTo(sa2));
        assertThat(sa1, not(equalTo(sa3)));
        assertThat(sa3, equalTo(sa4));
        assertThat(da1, equalTo(da2));
        assertThat(da2, not(equalTo(da3)));
        
    }
}
