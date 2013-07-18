package no.met.metadataeditor.utils;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import no.met.metadataeditor.util.SkosUtils;

import org.junit.Test;

public class SkosUtilsTest {

    
    @Test
    public void testGetControlledVocab() {
     
        InputStream is = SkosUtilsTest.class.getResourceAsStream("/utils/activity_types.rdf");
        
        List<String> controlledVocab = SkosUtils.getControlledVocab(is);
        
        List<String> expected = Arrays.asList("Aircraft", "Climate Indicator", "Space Borne Instrument");
        
        assertEquals(expected, controlledVocab);
        
        
    }
    
}
