package no.met.metadataeditor.dataTypes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EditorTemplate {
    private Map<String, EditorVariable> template;
    private Map<String, String> prefixeNamspace;
    private Map<String, String> namespacePrefixes;

    public EditorTemplate(InputSource source) throws SAXException, IOException {
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
        template = th.getResultConfig();
        namespacePrefixes = th.getNamespacePrefixes();
        prefixeNamspace = new HashMap<String, String>();
        for (String key : namespacePrefixes.keySet()) {
            prefixeNamspace.put(namespacePrefixes.get(key), key);
        }
    }

    NamespaceContext getTemplateContext() {
        return new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                return prefixeNamspace.get(prefix);
            }

            public String getPrefix(String namespaceURI) {
                return namespacePrefixes.get(namespaceURI);
            }

            public Iterator getPrefixes(String namespaceURI) {
                // only one prefix by construction
                List<String> prefixes = new ArrayList<String>();
                String prefix = getPrefix(namespaceURI);
                if (prefix != null) {
                    prefixes.add(prefix);
                }
                return prefixes.iterator();
            }
        };
    }

    public void addData(InputSource xmlData) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlData);
        XPathFactory xpathFact = XPathFactory.newInstance();
        XPath xpath = xpathFact.newXPath();
        xpath.setNamespaceContext(getTemplateContext());

        // TODO: retrieve the information for the EditorVariables

    }

    public Map<String, EditorVariable> getTemplate() {
        return template;
    }


}
