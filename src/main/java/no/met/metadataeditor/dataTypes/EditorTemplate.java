package no.met.metadataeditor.dataTypes;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import no.met.metadataeditor.EditorException;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;

import org.apache.commons.io.IOUtils;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class EditorTemplate {
    private Map<String, EditorVariable> varMap;
    private Map<String, String> prefixeNamspace;
    private Map<String, String> namespacePrefixes;

    private static Map<String,Class<? extends DataAttribute>> supportedTags;

    private String templateXML;


    public EditorTemplate(InputSource source) throws SAXException, IOException {

        // we read the entire XML contents from the source so that we can re-use it later.
        if( source.getCharacterStream() != null ){
            templateXML = IOUtils.toString(source.getCharacterStream());
        } else {
            templateXML = IOUtils.toString(source.getByteStream());
        }


        TemplateHandler th = new TemplateHandler();
        th.setNamespacePrefixes(findNamespaces(templateXML));
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try {
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(th);
            xmlReader.parse(new InputSource(new StringReader(templateXML)));
        } catch (ParserConfigurationException e) {
            throw new EditorException("Parsing template failed.", e, EditorException.TEMPLATE_PARSE_ERROR);
        }
        varMap = th.getResultConfig();

        namespacePrefixes = th.getNamespacePrefixes();
        prefixeNamspace = new HashMap<String, String>();
        for (String key : namespacePrefixes.keySet()) {
            prefixeNamspace.put(namespacePrefixes.get(key), key);
        }

        supportedTags = TemplateHandler.getSupportedTags();


    }

    NamespaceContext getTemplateContext() {
        return new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return prefixeNamspace.get(prefix);
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return namespacePrefixes.get(namespaceURI);
            }

            @Override
            @SuppressWarnings("rawtypes")
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
     * Find all the declared namespaces in a XML document.
     * @param templateXML The XML content to search for namespaces in
     * @return A mapping between namespace URIs and namespace prefixes.
     */
    static Map<String,String> findNamespaces(String templateXML){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db;
        Map<String,String> namespaces = new HashMap<String,String>();
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(IOUtils.toInputStream(templateXML));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression xPathExpression = xPath.compile("//namespace::*");
            NodeList namespaceNodes = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);

            for( int i = 0; i < namespaceNodes.getLength(); i++ ){
                Node node = namespaceNodes.item(i);

                // we skip the 'xml' namespace
                if(!"xml".equals(node.getLocalName())){
                    namespaces.put(node.getNodeValue(), node.getLocalName());
                }
            }

        } catch (ParserConfigurationException e) {
            String msg = "Failed to created parser";
            Logger.getLogger(EditorTemplate.class.getName()).log(Level.SEVERE, msg);
            throw new EditorException("Failed to create parser", e, EditorException.TEMPLATE_PARSE_ERROR);
        } catch (SAXException e) {
            String msg = "SAX error when finding namespaces";
            Logger.getLogger(EditorTemplate.class.getName()).log(Level.SEVERE, msg);
            throw new EditorException("Failed to create parser", e, EditorException.TEMPLATE_PARSE_ERROR);
        } catch (IOException e) {
            String msg = "IOException when fidning namespaces";
            Logger.getLogger(EditorTemplate.class.getName()).log(Level.SEVERE, msg);
            throw new EditorException("Failed to create parser", e, EditorException.TEMPLATE_PARSE_ERROR);
        } catch (XPathExpressionException e) {
            String msg = "XPath problem when finding namespaces";
            Logger.getLogger(EditorTemplate.class.getName()).log(Level.SEVERE, msg);
            throw new EditorException("Failed to create parser", e, EditorException.TEMPLATE_PARSE_ERROR);
        }

        return namespaces;

    }

    public Map<String,List<EditorVariableContent>> getContent(InputSource xmlData) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlData);
        XPathFactory xpathFact = XPathFactory.newInstance();
        XPath xpath = xpathFact.newXPath();
        xpath.setNamespaceContext(getTemplateContext());

        // retrieve the information for the EditorVariables
        return readEditorContent(xpath, "", doc, getVarMap());
    }

    /**
     * recursively read the information in the xml-file, starting at the top-node
     *
     * @param xpath xpath with correct namespace-context
     * @param nodeXpath the elements will be searched for below the current element
     * @param node the node below the Editorvariables should be searched
     * @param vars a map of editorVariables
     */
    private Map<String, List<EditorVariableContent>> readEditorContent(XPath xpath, String nodePath, Node node, Map<String, EditorVariable> vars) {

        Map<String,List<EditorVariableContent>> content = new HashMap<String,List<EditorVariableContent>>();
        for (String varName : vars.keySet()) {
            List<EditorVariableContent> contentList = new ArrayList<EditorVariableContent>();
            content.put(varName, contentList);

            EditorVariable ev = vars.get(varName);

            String selectionPath = ev.getSelectionXPath();

            // The variable does not have hard coded selection path (the most common case)
            // then select using document path.
            if( selectionPath == null ){
                selectionPath = ev.getDocumentXPath();
            }


            selectionPath = selectionPath.substring(nodePath.length());
            if (selectionPath.startsWith("/")) {
                selectionPath = selectionPath.substring(1);
            }

            try {
                Logger.getLogger(getClass().getName()).fine(String.format("EditorVariable %s with path %s and local path %s", varName, ev.getDocumentXPath(), selectionPath));
                XPathExpression expr =  xpath.compile(selectionPath);
                NodeList evSubnodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
                for (int i = 0; i < evSubnodes.getLength(); ++i) {

                    Node subNode = evSubnodes.item(i);
                    DataAttribute da = readAttributes(ev, subNode, xpath);
                    EditorVariableContent evc = new EditorVariableContent();
                    evc.setAttrs(da);

                    Map<String, List<EditorVariableContent>> children = readEditorContent(xpath, ev.getDocumentXPath(), subNode, ev.getChildren());
                    evc.setChildren(children);
                    contentList.add(evc);

                }
            } catch (XPathExpressionException e) {
                // should never happen
                throw new EditorException("Failed to evaluate XPath expression: " + selectionPath, e, EditorException.GENERAL_ERROR_CODE);
            }
        }
        return content;
    }

    private DataAttribute readAttributes(EditorVariable variable, Node node, XPath xpath) {

        DataAttribute da = variable.getDataAttributes().newInstance();
        // set the attributes
        Map<String, String> attXpath = variable.getAttrsXPath();
        for (String att : attXpath.keySet()) {
            String relAttPath = attXpath.get(att).substring(variable.getDocumentXPath().length());
            Logger.getLogger(getClass().getName()).fine(String.format("searching attr %s in %s", att, relAttPath));
            if (relAttPath.startsWith("/")) {
                // remove leading / in e.g. /text()
                relAttPath = relAttPath.substring(1);
            }
            XPathExpression attExpr;
            try {
                attExpr = xpath.compile(relAttPath);
                String attVal = attExpr.evaluate(node);
                Logger.getLogger(getClass().getName()).fine(String.format("%s + value = %s", relAttPath, attVal));
                da.addAttribute(att, attVal);
            } catch (XPathExpressionException e) {
                // should never happen
                throw new EditorException("Failed to evaluate XPath expression when getting the actual attributes values:" + relAttPath, e, EditorException.GENERAL_ERROR_CODE );
            }

        }

        return da;

    }

    /**
     * Write the the content of editor variables to the template to produce a new XML file.
     * @param templateXML The template
     * @param content A map of content for the variables in the template.
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public org.jdom2.Document writeContent(Map<String, List<EditorVariableContent>> content) throws JDOMException, IOException  {

        SAXBuilder builder = new SAXBuilder();

        org.jdom2.Document templateDoc = builder.build(new InputSource(new StringReader(templateXML)));
        TemplateNode rootNode = genTemplateTree(templateDoc, content);

        org.jdom2.Document doc = replaceVars(rootNode);
        pruneTree(doc);

        return doc;

    }

    /**
     * Remove the editor variables nodes from the tree.
     * @param doc
     */
    private void pruneTree(org.jdom2.Document doc){
        pruneTreeRecursive(doc.getRootElement(), null, doc);
    }

    /**
     * Prune the tree recursively by making all children of editor variable nodes point to their grandparent
     * or if there is no grandparent attach them to the document directly, even though this leads to an invalid XML document.
     * @param element The element to process
     * @param parent The parent of the processed element
     * @param doc The document
     */
    private void pruneTreeRecursive(Element element, Element parent, org.jdom2.Document doc){

        List<Content> children = new ArrayList<Content>();
        for( Content child : element.getContent()){
            children.add(child);
        }

        if( supportedTags.containsKey(element.getName())){

            // need the index of the element to know where to insert the elements
            // children
            int elementIndex;
            if( parent != null ){
                elementIndex = parent.indexOf(element);
            } else {
                elementIndex = doc.indexOf(element);
            }

            element.detach();
            for( Content child : children ){

                if( child instanceof Element ){
                    Element e = (Element) child;
                    pruneTreeRecursive(e, parent, doc);
                }

                child.detach();
                if( parent != null ){
                    parent.addContent(elementIndex++, child);
                } else {
                    doc.addContent(elementIndex++, child);
                }
            }
        } else {
            for( Content child : children ){

                if( child instanceof Element ){
                    Element e = (Element) child;
                    pruneTreeRecursive(e, element, doc);
                }
            }
        }
    }

    /**
     * Generate a tree that combines the information from the template document and the variable content. The generated
     * tree will be expanded with nodes depending on the information on the variables.
     * @param templateDoc
     * @param content
     * @return
     */
    private TemplateNode genTemplateTree(org.jdom2.Document templateDoc, Map<String, List<EditorVariableContent>> content) {

        // skip the top node since it is part of the editor template and not the XML we want
        // in the end.
        Element templateRoot = templateDoc.getRootElement().getChildren().get(0);

        TemplateNode trn = new TemplateNode();
        trn.children = genTemplateTreeRecursive(templateRoot, content);
        trn.xmlNode = templateRoot;

        return trn;
    }

    private List<TemplateNode> genTemplateTreeRecursive(Element element, Map<String, List<EditorVariableContent>> contentMap){

        List<TemplateNode> children = new ArrayList<TemplateNode>();

        for( Content c : element.getContent() ){

            if( c instanceof Element ){
                Element child = (Element) c;
                if( supportedTags.containsKey(child.getName()) ){

                    String varName = child.getAttributeValue("varName");
                    List<EditorVariableContent> contentList = contentMap.get(varName);

                    // there is not content to fill in for this variable, so we should skip it.
                    if( contentList == null ){
                        continue;
                    }

                    for( EditorVariableContent evc : contentList ){

                        TemplateVarNode tvn = new TemplateVarNode();
                        tvn.content = evc;
                        tvn.children = genTemplateTreeRecursive(child, evc.getChildren());
                        tvn.xmlNode = child;

                        children.add(tvn);
                    }

                } else {
                    TemplateNode tn = new TemplateXMLNode();
                    tn.children = genTemplateTreeRecursive(child, contentMap);
                    tn.xmlNode = child;
                    children.add(tn);

                }
            } else {
                TemplateNode tn = new TemplateXMLNode();
                tn.children = new ArrayList<TemplateNode>();
                tn.xmlNode = c;
                children.add(tn);
            }
        }

        return children;

    }

    private org.jdom2.Document replaceVars(TemplateNode root) {

        org.jdom2.Document doc = new org.jdom2.Document();
        Content c = replaceVarsRecursive(root);
        doc.setContent(c);
        return doc;
    }

    private Content replaceVarsRecursive(TemplateNode node){

        Content c = node.xmlNode.clone();

        if( c instanceof Element ){
            Element e = (Element) c;
            e.getContent().clear();
            for( TemplateNode n : node.children ) {
                Content child = replaceVarsRecursive(n);
                e.addContent(child);
            }

        }

        if( node instanceof TemplateVarNode ){
            TemplateVarNode tvn = (TemplateVarNode) node;
            replace(c, tvn.content);
        }
        return c;
    }

    private void replace(Content c, EditorVariableContent evc ){

        if( c instanceof Text ){
            Text text = (Text) c;
            String currValue = text.getText();
            text.setText(getReplaceValue(currValue, evc));
        } else if ( c instanceof Element ){
            Element e = (Element) c;

            for( Attribute a : e.getAttributes()){

                String currValue = a.getValue();
                a.setValue(getReplaceValue(currValue, evc));
            }

            for( Content childContent : e.getContent()){
                replace(childContent, evc);
            }
        }
    }

    private String getReplaceValue(String value, EditorVariableContent evc){

        DataAttribute da = evc.getAttrs();
        for( String attrKey : da.getAttributesSetup().keySet() ){
            String newValue = da.getAttribute(attrKey);
            value = value.replace("$" + attrKey, newValue);
        }
        return value;

    }


    public Map<String, EditorVariable> getVarMap() {
        return varMap;
    }


}
