package no.met.metadataeditor.dataTypes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class TemplateHandler extends DefaultHandler {

    final String EDT = "http://www.met.no/schema/metadataeditor/editorDataTypes";
    Deque<EditorVariable> edtElements;
    Deque<String> fullPathElements;
    Deque<String> finalPathElements; // without EDT-elements
    private Map<String, EditorVariable> resultConfig;

    public void startDocument() throws SAXException {
        edtElements  = new ArrayDeque<EditorVariable>();
        fullPathElements = new ArrayDeque<String>();
        finalPathElements = new ArrayDeque<String>();
        resultConfig = null;
    }

    private static String variableAddAttributes(EditorVariable ev, Attributes atts) throws SAXException {
        String maxOccurs = atts.getValue("maxOccurs");
        if (maxOccurs == null) {
            ev.setMaxOccurs(1);
        }else if ("unbounded".equals(maxOccurs)) {
            ev.setMaxOccurs(Integer.MAX_VALUE);
        } else {
            ev.setMaxOccurs(Integer.parseInt(maxOccurs));
        }
        String minOccurs = atts.getValue("minOccurs");
        if (minOccurs == null) {
            ev.setMinOccurs(1);
        } else {
            ev.setMinOccurs(Integer.parseInt(minOccurs));
        }
        String res = atts.getValue("resource");
        if (res != null) {
            URI uri;
            try {
                uri = new URI(res);
            } catch (URISyntaxException e) {
                throw new SAXNotRecognizedException(e.toString());
            }
            ev.addResource("default", uri);
        }

        return atts.getValue("varName");
    }

    private void addStandardEDT(DataAttributes da, Attributes atts) throws SAXException {
        EditorVariable ev = new EditorVariable(da);
        String varName = variableAddAttributes(ev, atts);
        edtElements.getLast().addChild(varName, ev);
        edtElements.addLast(ev);
    }

    public void startElement(String nsUri, String lName, String qName, Attributes atts) throws SAXException {
        fullPathElements.addLast(qName);
        if (EDT.equals(nsUri)) {
            if ("editorDataTypes".equals(lName)) {
                //  start element, empty container
                assert(edtElements.size() == 0);
                EditorVariable ev = new EditorVariable(new NullAttributes());
                edtElements.addLast(ev);
            } else if ("container".equals(lName)) {
                addStandardEDT(new NullAttributes(), atts);
            } else if ("lonLatBoundingBox".equals(lName)) {
                addStandardEDT(new LatLonBBAttributes(), atts);
            } else if ("string".equals(lName)) {
                addStandardEDT(new StringAttributes(), atts);
            } else if ("uri".equals(lName)) {
                addStandardEDT(new UriAttributes(), atts);
            } else if ("list".equals(lName)) {
                addStandardEDT(new ListElementAttributes(), atts);
            } else if ("stringAndList".equals(lName)) {
                addStandardEDT(new StringAndListElementAttributes(), atts);
            } else if ("startAndStopTime".equals(lName)) {
                addStandardEDT(new StartAndStopTimeAttributes(), atts);
            } else {
                throw new UndefinedEditorVariableException(lName);
            }
        } else {
            finalPathElements.addLast(qName);
        }

    }

    public void endElement(String nsUri, String lName, String qName) {
        fullPathElements.removeLast();
        if (EDT.equals(nsUri)) {
            if ("editorDataTypes".equals(lName)) {
                assert(edtElements.size() == 1);
                resultConfig = edtElements.getFirst().getChildren();
            }
            edtElements.removeLast();
        } else {
            finalPathElements.removeLast();
        }
    }

    private Map<String, EditorVariable> getResultConfig() {
        return resultConfig;
    }

    /**
     * parse a template file and get the configuration as map of objects
     *
     * @param source
     * @return map of varNames to EditorVariables, null on error
     * @throws SAXException
     * @throws IOException
     */
    public static Map<String, EditorVariable> parseTemplate(InputSource source) throws SAXException, IOException {
        TemplateHandler th = new TemplateHandler();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(th);
            xmlReader.parse(source);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return th.getResultConfig();
    }
}
