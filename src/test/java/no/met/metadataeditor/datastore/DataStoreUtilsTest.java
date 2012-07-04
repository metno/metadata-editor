package no.met.metadataeditor.datastore;

import static org.junit.Assert.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import static no.met.metadataeditor.TestHelpers.*;

public class DataStoreUtilsTest {

    
    @Test
    public void testGetFormatMM2Combined() throws XMLStreamException, FactoryConfigurationError{
      
        String metadata = formattedXMLAsString("/datastore/datastoreutils/mm2combined.xml");
        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        assertEquals(SupportedFormat.MM2COMBINED, format);
    }
    
    @Test
    public void testGetFormatMM2() throws XMLStreamException, FactoryConfigurationError{
      
        String metadata = formattedXMLAsString("/datastore/datastoreutils/mm2.xml");
        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        assertEquals(SupportedFormat.MM2, format);
    }
    
    @Test
    public void testGetFormatISO() throws XMLStreamException, FactoryConfigurationError{
      
        String metadata = formattedXMLAsString("/datastore/datastoreutils/iso.xml");
        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        assertEquals(SupportedFormat.ISO19139, format);
    }
    
    @Test
    public void testGetFormatISOCombined() throws XMLStreamException, FactoryConfigurationError{
      
        String metadata = formattedXMLAsString("/datastore/datastoreutils/isocombined.xml");
        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        assertEquals(SupportedFormat.ISO19139COMBINED, format);
    }    
      
  
    
}
