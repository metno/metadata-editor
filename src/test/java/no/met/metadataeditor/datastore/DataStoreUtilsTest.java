package no.met.metadataeditor.datastore;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import static no.met.metadataeditor.TestHelpers.*;

public class DataStoreUtilsTest {

    List<SupportedFormat> formats;

    @Before
    public void setUp() {
        String supported = "#test comment line\n"+
            "MM2         MM2             http://www.met.no/schema/metamod/MM2\n"+
            "MM2COMBINED mmCombinedMM2   http://www.met.no/schema/metamod/mmCombined\n" +
            "ISO19139    MD_Metadata     http://www.isotc211.org/2005/gmd\n" +
            "ISO19139COMBINED    mmCombinedISO http://www.met.no/schema/metamod/mmCombined";
        formats = DataStoreUtils.parseSupportedFormats(supported);
    }

    @Test
    public void testParseSupportedFormats() {
        assertEquals(4, formats.size());
        assertEquals("http://www.met.no/schema/metamod/MM2", formats.get(0).getNamespace());
    }

    @Test
    public void testGetFormatMM2Combined() throws XMLStreamException, FactoryConfigurationError{

        String metadata = formattedXMLAsString("/datastore/datastoreutils/mm2combined.xml");
        SupportedFormat format = DataStoreUtils.getFormat(formats, metadata);
        assertEquals("MM2COMBINED", format.getTagName());
    }

    @Test
    public void testGetFormatMM2() throws XMLStreamException, FactoryConfigurationError{

        String metadata = formattedXMLAsString("/datastore/datastoreutils/mm2.xml");
        SupportedFormat format = DataStoreUtils.getFormat(formats, metadata);
        assertEquals("MM2", format.getTagName());
    }

    @Test
    public void testGetFormatISO() throws XMLStreamException, FactoryConfigurationError{

        String metadata = formattedXMLAsString("/datastore/datastoreutils/iso.xml");
        SupportedFormat format = DataStoreUtils.getFormat(formats, metadata);
        assertEquals("ISO19139", format.getTagName());
    }

    @Test
    public void testGetFormatISOCombined() throws XMLStreamException, FactoryConfigurationError{

        String metadata = formattedXMLAsString("/datastore/datastoreutils/isocombined.xml");
        SupportedFormat format = DataStoreUtils.getFormat(formats, metadata);
        assertEquals("ISO19139COMBINED", format.getTagName());
    }



}
