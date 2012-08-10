package no.met.metadataeditor.datastore;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import no.met.metadataeditor.EditorException;

/**
 * Utility functions for DataStore classes.
 */
public class DataStoreUtils {

    /**
     * Find the XML format used by the a metadata file by looking at the first tag in the file.
     * @param metadataXML The raw XML for the metadata file.
     * @throws IllegalArgumentException Thrown if the format is not known.
     * @return The format used by the XML file.
     */
    public static SupportedFormat getFormat(List<SupportedFormat> formats, String metadataXML){

        StringReader metadataReader = new StringReader(metadataXML);
        XMLStreamReader reader;
        SupportedFormat format = null;
        try {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(metadataReader);
            while( reader.hasNext() ){

                int code = reader.next();
                if( code == XMLStreamConstants.START_ELEMENT ){

                    String rootNode = reader.getLocalName();
                    String namespace = reader.getNamespaceURI();
                    format = testFormats(formats, rootNode, namespace);

                    // we are only interested in the first element.
                    break;
                }
            }
        } catch (XMLStreamException e) {
            throw new EditorException(e.getMessage());
        } catch (FactoryConfigurationError e) {
            throw new EditorException(e.getMessage());
        }

        return format;
    }

    private static SupportedFormat testFormats(List<SupportedFormat> formats, String rootNode, String namespace) {
        SupportedFormat format = null;
        for (SupportedFormat f : formats) {
            if (f.matches(rootNode, namespace)) {
                format = f;
                break;
            }
        }
        if (format == null) {
            throw new IllegalArgumentException("The metadata is not in one of the supported formats");
        }
        return format;
    }

    static List<SupportedFormat> parseSupportedFormats(Document doc) {
        List<SupportedFormat> formats = new ArrayList<SupportedFormat>();
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile("//supportedMetadataFormats/format");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String tag = xpath.evaluate("@tag", node);
                NodeList detectors = (NodeList) xpath.evaluate("detector", node, XPathConstants.NODESET);
                if (detectors.getLength() != 1) {
                    throw new EditorException(String.format("found %d detectors, need exactly 1 for format %s",
                            detectors.getLength(), tag));
                }
                String detectorType = xpath.evaluate("@type", detectors.item(0));
                if (!"rootNode".equals(detectorType)) {
                    throw new EditorException(String.format(
                            "found detector-type '%s' for format '%s', currently only 'rootNode' allowed in %s",
                            detectorType, tag));
                }
                String rootNode = xpath.evaluate("arg[@name='rootNode']/@value", detectors.item(0));
                String namespace = xpath.evaluate("arg[@name='namespace']/@value", detectors.item(0));
                formats.add(new SupportedFormat(tag, rootNode, namespace));
            }
        } catch (XPathException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return formats;
    }

}
