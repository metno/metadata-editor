package no.met.metadataeditor.datastore;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import static no.met.metadataeditor.TestHelpers.*;

public class DataStoreUtilsTest {

    List<SupportedFormat> formats = new ArrayList<SupportedFormat>();

    @Before
    public void setUp() {
        Document doc;
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        try {
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(DataStoreUtilsTest.class.getResourceAsStream("/datastore/diskdatastore/testProject/config/setup.xml"));
            formats = DataStoreUtils.parseSupportedFormats(doc);
        } catch (SAXException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        }
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
