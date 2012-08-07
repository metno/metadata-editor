package no.met.metadataeditor.datastore;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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

    public static List<SupportedFormat> parseSupportedFormats(String supportedFormats) {
        List<SupportedFormat> formats = new ArrayList<SupportedFormat>();
        Pattern p = Pattern.compile("^(?!#)(\\S+)\\s+(\\S+)(\\s+(\\S+))?\\s*$", Pattern.MULTILINE);
        Matcher m = p.matcher(supportedFormats);
        while (m.find()) {
            String tag = m.group(1);
            String node = m.group(2);
            String namespace = null;
            if (m.groupCount() >= 4)
                namespace = m.group(4);
            formats.add(new SupportedFormat(tag, node, namespace));
        }

        return formats;
    }

}
