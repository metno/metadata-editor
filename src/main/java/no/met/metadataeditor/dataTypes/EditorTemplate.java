package no.met.metadataeditor.dataTypes;

import java.io.IOException;
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
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.istack.logging.Logger;

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

    /**
     * recursively read the information in the xml-file, starting at the top-node
     *
     * @param xpath xpath with correct namespace-context
     * @param nodeXpath the elements will be searched for below the current element
     * @param node the node below the Editorvariables should be searched
     * @param vars a map of editorVariables
     */
    private void readEditorVariables(XPath xpath, String nodePath, Node node, Map<String, EditorVariable> vars) {
        for (String varName : vars.keySet()) {
            EditorVariable ev = vars.get(varName);
            String evPath = ev.getDocumentXPath().substring(nodePath.length());
            try {
                Logger.getLogger(EditorTemplate.class).fine(String.format("EditorVariable %s with path %s and local path %s", varName, ev.getDocumentXPath(), evPath));
                XPathExpression expr =  xpath.compile(evPath);
                NodeList evSubnodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
                for (int i = 0; i < evSubnodes.getLength(); ++i) {
                    DataAttributes da = ev.getDataAttributes();
                    Node subNode = evSubnodes.item(i);
                    // set the attributes
                    Map<String, String> attXpath = ev.getAttrsXPath();
                    for (String att : attXpath.keySet()) {
                        String relAttPath = attXpath.get(att).substring(ev.getDocumentXPath().length());
                        XPathExpression attExpr = xpath.compile(relAttPath);
                        String attVal = attExpr.evaluate(subNode);
                        da.addAttribute(att, attVal);
                    }
                    EditorVariableContent evc = new EditorVariableContent();
                    evc.setAttrs(da);
                    evc.setChildren(ev.getChildren());
                    ev.addContent(evc);
                    // and fill the children
                    readEditorVariables(xpath, evPath, subNode, ev.getChildren());
                }
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void addData(InputSource xmlData) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlData);
        XPathFactory xpathFact = XPathFactory.newInstance();
        XPath xpath = xpathFact.newXPath();
        xpath.setNamespaceContext(getTemplateContext());

        // retrieve the information for the EditorVariables
        readEditorVariables(xpath, "", doc, getTemplate());
    }

    public Map<String, EditorVariable> getTemplate() {
        return template;
    }


}
