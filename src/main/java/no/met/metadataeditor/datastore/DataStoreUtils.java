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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import no.met.metadataeditor.EditorException;
import no.met.metadataeditor.validationclient.SimplePutValidationClient;
import no.met.metadataeditor.validationclient.ValidationClient;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        } catch (XMLStreamException | FactoryConfigurationError e) {
            throw new EditorException(e.getMessage(), e, EditorException.METADATA_PARSE_ERROR);
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
                Node formatNode = nodes.item(i);
                String tag = xpath.evaluate("@tag", formatNode);
                NodeList detectors = (NodeList) xpath.evaluate("detector", formatNode, XPathConstants.NODESET);
                if (detectors.getLength() != 1) {
                    throw new EditorException(String.format("found %d detectors, need exactly 1 for format %s",
                            detectors.getLength(), tag), EditorException.SETUP_XML_ERROR);
                }
                String detectorType = xpath.evaluate("@type", detectors.item(0));
                if (!"rootNode".equals(detectorType)) {
                    throw new EditorException(String.format(
                            "found detector-type '%s' for format '%s', currently only 'rootNode' allowed in %s",
                            detectorType, tag), EditorException.SETUP_XML_ERROR);
                }
                String rootNode = xpath.evaluate("arg[@name='rootNode']/@value", detectors.item(0));
                String namespace = xpath.evaluate("arg[@name='namespace']/@value", detectors.item(0));

                ValidationClient validationClient = parseValidationPart(formatNode, xpath);

                formats.add(new SupportedFormat(tag, rootNode, namespace, validationClient));
            }
        } catch (XPathException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return formats;
    }

    private static ValidationClient parseValidationPart(Node formatNode, XPath xpath) throws XPathExpressionException{

        Node validatorNode = (Node) xpath.evaluate("validator", formatNode, XPathConstants.NODE);

        if( validatorNode == null ){
            return null;
        }

        String validatorType = xpath.evaluate("@type", validatorNode);

        if("simplePutService".equals(validatorType)){
            String url = xpath.evaluate("arg[@name='URL']/@value", validatorNode);
            ValidationClient client = new SimplePutValidationClient(url);
            return client;
        }

        throw new EditorException("Invalid type for validation: " + validatorType, EditorException.SETUP_XML_ERROR );

    }

}
