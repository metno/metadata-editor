package no.met.metadataeditor.dataTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.istack.logging.Logger;
import com.sun.research.ws.wadl.Doc;

public class EditorTemplate {
    private Map<String, EditorVariable> template;
    private Map<String, String> prefixeNamspace;
    private Map<String, String> namespacePrefixes;

    private static Map<String,String> templateTags = new HashMap<String,String>();
    
    static {
        templateTags.put("container", "container");
        templateTags.put("lonLatBoundingBox", "lonLatBoundingBox");
        templateTags.put("string", "string" );
        templateTags.put("uri", "uri");
        templateTags.put("list", "list");
        templateTags.put("stringAndList", "stringAndList");
        templateTags.put("startAndStopTime", "startAndStopTime");    
    }
    
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

  
    public Map<String,List<EditorVariableContent>> getContent(InputSource xmlData) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlData);
        XPathFactory xpathFact = XPathFactory.newInstance();
        XPath xpath = xpathFact.newXPath();
        xpath.setNamespaceContext(getTemplateContext());

        // retrieve the information for the EditorVariables
        return readEditorContent(xpath, "", doc, getTemplate());
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
            String evPath = ev.getDocumentXPath().substring(nodePath.length());
            if (evPath.startsWith("/")) {
                evPath = evPath.substring(1);
            }
            
            try {
                Logger.getLogger(EditorTemplate.class).fine(String.format("EditorVariable %s with path %s and local path %s", varName, ev.getDocumentXPath(), evPath));
                XPathExpression expr =  xpath.compile(evPath);
                NodeList evSubnodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
                for (int i = 0; i < evSubnodes.getLength(); ++i) {

                    Node subNode = evSubnodes.item(i);
                    DataAttributes da = readAttributes(ev, subNode, xpath);                    
                    EditorVariableContent evc = new EditorVariableContent();
                    evc.setAttrs(da);

                    Map<String, List<EditorVariableContent>> children = readEditorContent(xpath, ev.getDocumentXPath(), subNode, ev.getChildren());
                    evc.setChildren(children);
                    contentList.add(evc);

                }
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return content;
    }    
    
    private DataAttributes readAttributes(EditorVariable variable, Node node, XPath xpath) throws XPathExpressionException {

        DataAttributes da = variable.getDataAttributes().newInstance();
        // set the attributes
        Map<String, String> attXpath = variable.getAttrsXPath();
        for (String att : attXpath.keySet()) {
            String relAttPath = attXpath.get(att).substring(variable.getDocumentXPath().length());
            Logger.getLogger(EditorTemplate.class).fine(String.format("searching attr %s in %s", att, relAttPath));
            if (relAttPath.startsWith("/")) {
                // remove leading / in e.g. /text()
                relAttPath = relAttPath.substring(1);
            }
            XPathExpression attExpr = xpath.compile(relAttPath);
            String attVal = attExpr.evaluate(node);
            Logger.getLogger(EditorTemplate.class).fine(String.format("%s + value = %s", relAttPath, attVal));
            da.addAttribute(att, attVal);
        }
        
        return da;
        
    }

    public org.jdom2.Document writeContent(InputSource templateXML, Map<String, List<EditorVariableContent>> content) throws ParserConfigurationException, SAXException, IOException {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setNamespaceAware(true);
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document templateDoc = db.parse(templateXML);
//        XPathFactory xpathFact = XPathFactory.newInstance();
//        XPath xpath = xpathFact.newXPath();
//        xpath.setNamespaceContext(getTemplateContext());

        SAXBuilder builder = new SAXBuilder();
        TemplateRootNode root = null;
        try {
            org.jdom2.Document d = builder.build(templateXML);
            root = genTemplateTree2(d, content);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        org.jdom2.Document doc = replaceVars(root);
        pruneTree(doc);
        
        return doc;

//        Map<String, EditorVariable> template = getTemplate();            
//        
//        Document resultDocument = db.newDocument();
//        Node templateRoot = templateDoc.getFirstChild().getFirstChild();
//        Node imported = resultDocument.importNode(templateRoot, false);
//        resultDocument.appendChild(imported);
//        
//        // retrieve the information for the EditorVariables
//        writeEditorContent2(resultDocument, xpath, "", templateDoc, template, content);
//        
//        tmp(templateDoc);
//        Document resultDoc = db.newDocument();
//        //resultDoc.appendChild(rootNode);
//        
////        String xml = write(templateDoc, rootTemplate, content, xpath);
////        Document resultDoc = null;        
////        try {
////            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
////            resultDoc = db.parse(is);
////            
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
//        
//
//        
//        return resultDocument;
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer;
//        try {
//            transformer = tf.newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            StringWriter writer = new StringWriter();
//            transformer.transform(new DOMSource(doc), new StreamResult(writer));
//            String output = writer.getBuffer().toString();
//            System.out.println(output);
//        } catch (TransformerConfigurationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (TransformerException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        
    }       
    
    private void pruneTree(org.jdom2.Document doc){
        
        pruneTreeRecursive(doc.getRootElement(), null, doc);
        
        
    }
    
    private List<Content> pruneTreeRecursive(Element element, Element parent, org.jdom2.Document doc){
        
       
        if( templateTags.containsKey(element.getName())){
            
            element.detach();
            for( Element child : element.getChildren() ){

                
                pruneTreeRecursive(child, parent, doc);

                child.detach();
                if( parent != null ){
                    parent.addContent(child);
                } else {
                    doc.addContent(child);
                }                
            }
        } else {
            
            List<Element> children = new ArrayList<Element>();
            for( Element child : element.getChildren()){
                children.add(child);
            }
            
            for( Element child : children ){
                pruneTreeRecursive(child, element, doc);
            }
        }
        return null;
    }
    
    private TemplateRootNode genTemplateTree2(org.jdom2.Document templateDoc, Map<String, List<EditorVariableContent>> content) {

        // skip the top node since it is part of the editor template and not the XML we want
        // in the end.
        Element templateRoot = templateDoc.getRootElement().getChildren().get(0);        
        
        TemplateRootNode trn = new TemplateRootNode();
        trn.children = genTemplateTreeRecursive2(templateRoot, content);
        trn.xmlNode = templateRoot;
        
        return trn;
    }
    
    private List<TemplateNode> genTemplateTreeRecursive2(Element element, Map<String, List<EditorVariableContent>> contentMap){
               
        List<TemplateNode> children = new ArrayList<TemplateNode>();

        for( Content c : element.getContent() ){
            
            if( c instanceof Element ){
                Element child = (Element) c;
                if( templateTags.containsKey(child.getName()) ){
                                                    
                    String varName = child.getAttributeValue("varName");
                    List<EditorVariableContent> contentList = contentMap.get(varName);
                    for( EditorVariableContent evc : contentList ){
    
                        TemplateVarNode tvn = new TemplateVarNode();
                        tvn.content = evc;
                        
                        tvn.children = genTemplateTreeRecursive2(child, evc.getChildren());
                        tvn.xmlNode = child;
                        
                        children.add(tvn);
                    }
                    
                } else {
                    TemplateNode tn = new TemplateXMLNode();
                    tn.children = genTemplateTreeRecursive2(child, contentMap);
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
    
    private org.jdom2.Document replaceVars(TemplateRootNode root) {
        
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
        
        DataAttributes da = evc.getAttrs();
        
        if( c instanceof org.jdom2.Text ){
            org.jdom2.Text text = (org.jdom2.Text) c;
            String currValue = text.getText();
            
            for( String attrKey : da.getAttributesSetup().keySet() ){
                String newValue = da.getAttribute(attrKey);
                currValue = currValue.replace("$" + attrKey, newValue);
            }
            text.setText(currValue);
        } else if ( c instanceof Element ){
            Element e = (Element) c;
            
            for( Attribute a : e.getAttributes()){

                String currValue = a.getValue();
                for( String attrKey : da.getAttributesSetup().keySet() ){
                    String newValue = da.getAttribute(attrKey);
                    currValue = currValue.replace("$" + attrKey, newValue);
                }
                a.setValue(currValue);
            }
            
            for( Content childContent : e.getContent()){
                replace(childContent, evc);
            }
        }
        
    }
    
    
    private TemplateRootNode genTemplateTree(Document templateDoc, Map<String, List<EditorVariableContent>> content) {

        Node templateRoot = templateDoc.getFirstChild();
        TemplateRootNode trn = new TemplateRootNode();
        trn.children = genTemplateTreeRecursive(templateRoot, content);
        
        return trn;
    }
    
    private List<TemplateNode> genTemplateTreeRecursive(Node node, Map<String, List<EditorVariableContent>> contentMap){
        
        Map<String,String> templateTags = new HashMap<String,String>();
        templateTags.put("container", "container");
        
        templateTags.put("lonLatBoundingBox", "lonLatBoundingBox");
        templateTags.put("string", "string" );
        templateTags.put("uri", "uri");
        templateTags.put("list", "list");
        templateTags.put("stringAndList", "stringAndList");
        templateTags.put("startAndStopTime", "startAndStopTime");
        
        List<TemplateNode> children = new ArrayList<TemplateNode>();
        
        for( int i = 0; i < node.getChildNodes().getLength(); i++ ){
            
            Node child = node.getChildNodes().item(i);
            if( child.getNodeType() == Node.ELEMENT_NODE && templateTags.containsKey(child.getLocalName()) ){
                                
                
                String varName = "";
                List<EditorVariableContent> contentList = contentMap.get(varName);
                for( EditorVariableContent evc : contentList ){

                    TemplateVarNode tvn = new TemplateVarNode();
                    tvn.content = evc;
                    
                    tvn.children = genTemplateTreeRecursive(child, evc.getChildren());
                    //tvn.xmlNode = child;
                    
                }
                
            } else {
                TemplateNode tn = new TemplateXMLNode();
                tn.children = genTemplateTreeRecursive(child, contentMap);
                //tn.xmlNode = child;
                children.add(tn);
                
            }
            
        }
        
        return children;
        
    }

    private String write( Node doc, Map<String, EditorVariable> vars, Map<String, List<EditorVariableContent>> content, XPath xpath ){
        
        String xml = "";
        
        for( Map.Entry<String, EditorVariable> entry : vars.entrySet() ){
            
            EditorVariable var = entry.getValue();
            
            List<EditorVariableContent> varContent = content.get(entry.getKey());
            
            Node templateNode = getTemplateNode(doc, entry.getValue(), xpath);            
            for( int i = 0; i < templateNode.getChildNodes().getLength(); i++ ){
                
                Node childNode = templateNode.getChildNodes().item(i);
                String childXML = nodeToString(childNode);
                
                if( varContent != null ) {
                    xml += replaceContent(childXML, varContent);
                } else {
                    xml += childXML;
                }
            }
            
            
            
            
        }
        
        
        return xml;
    }
    
    
    private String replaceContent( String nodeXML, List<EditorVariableContent> content){
        
        String outputXML = "";
        for( EditorVariableContent evc : content ){
            
            DataAttributes da = evc.getAttrs();
            String s = nodeXML;
            for( Map.Entry<String, DataType> attr : da.getAttributesSetup().entrySet()){
                
                String value = da.getAttribute(attr.getKey());
                s = s.replace("$" + attr.getKey(), value);
                
            }
            outputXML += s;
        }
        return outputXML;
    }
    
    
    private Node getTemplateNode(Node node, EditorVariable var, XPath xpath){

        String path = var.getTemplateXPath();
        XPathExpression expr;
        Node templateNode = null;
        try {
            expr = xpath.compile(path);
            templateNode = (Node) expr.evaluate(node, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
        return templateNode;
        
    }
    
    
    private Node tmp(Node node){
        
        for( int i = 0; i < node.getChildNodes().getLength(); i++ ){
            Node child = node.getChildNodes().item(i);
            Node newChild = tmp(child);
            
            System.out.println(child);
        }
        
        return null;
        
    }

    private void writeEditorContent2(Document resultDoc, XPath xpath, String nodePath, Document templateDoc, Map<String, EditorVariable> vars, Map<String, List<EditorVariableContent>> content) {
        
        for (String varName : vars.keySet()) {
            List<EditorVariableContent> contentList = content.get(varName);
            
            EditorVariable ev = vars.get(varName);
            String evPath = ev.getTemplateXPath().substring(nodePath.length());
            if (evPath.startsWith("/")) {
                evPath = evPath.substring(1);
            }
            
            try {
                XPathExpression expr =  xpath.compile(evPath);
                Node templateNode = (Node) expr.evaluate(templateDoc, XPathConstants.NODE);

                Node parent = templateNode.getParentNode();
                parent.removeChild(templateNode);
                
                Node child = templateNode.cloneNode(true);
                    //writeAttributes(ev, evc, child, xpath);
                    //parent.appendChild(child);
                    
                for( int i = 0; i < child.getChildNodes().getLength(); i++ ){
                
                    Node grandchild = child.getChildNodes().item(i);

                    if( grandchild.getNodeType() == Node.ELEMENT_NODE ){
                        String xml = nodeToString(grandchild);
                        
                        if( contentList != null){
                            xml = replaceContent(xml, contentList);
                        }
                        Node newChild = xmlToDocument(xml);
                        parent.appendChild(newChild);                        
                    } else {
                        Node clone = grandchild.cloneNode(true);             
                        parent.appendChild(clone);
                    }
                    
//                    if(grandchild.getNodeType() == Node.ELEMENT_NODE ){
//                        writeAttributes(ev, evc, grandchild, xpath);
//                        parent.appendChild(grandchild);                            
//                    } else {
//                        child.removeChild(grandchild);
//                    }
                }

            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }          
    
    
    private void writeEditorContent(XPath xpath, String nodePath, Node node, Map<String, EditorVariable> vars, Map<String, List<EditorVariableContent>> content) {
        
        for (String varName : vars.keySet()) {
            List<EditorVariableContent> contentList = content.get(varName);
            
            EditorVariable ev = vars.get(varName);
            String evPath = ev.getTemplateXPath().substring(nodePath.length());
            if (evPath.startsWith("/")) {
                evPath = evPath.substring(1);
            }
            
            try {
                XPathExpression expr =  xpath.compile(evPath);
                NodeList evSubnodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

                Node n = evSubnodes.item(0);
                Node parent = n.getParentNode();
                parent.removeChild(n);

                for( EditorVariableContent evc : contentList ){
                    
                    Node child = n.cloneNode(true);
                    //writeAttributes(ev, evc, child, xpath);
                    //parent.appendChild(child);
                    NodeList grandchildren = child.getChildNodes();
                    
                    Node grandchild;
                    while( (grandchild = child.getLastChild()) != null){
                        
                        if(grandchild.getNodeType() == Node.ELEMENT_NODE ){
                            writeAttributes(ev, evc, grandchild, xpath);
                            parent.appendChild(grandchild);                            
                        } else {
                            child.removeChild(grandchild);
                        }
                        
                    }
                    
                    
//                    int len = grandchildren.getLength();
//                    for( int i = 0; i < len; i++ ){
//                        Node grandChild = grandchildren.item(i);
//                        parent.appendChild(grandChild);
//                    }
                }
                
//                for (int i = 0; i < evSubnodes.getLength(); ++i) {
//
//                    Node subNode = evSubnodes.item(i);
//                    DataAttributes da = writeAttributes(ev, subNode, xpath);                    
//                    EditorVariableContent evc = new EditorVariableContent();
//                    evc.setAttrs(da);
//
//                    //Map<String, List<EditorVariableContent>> children = readEditorContent(xpath, ev.getDocumentXPath(), subNode, ev.getChildren());
//
//                }
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }      
    
    
    private void writeAttributes(EditorVariable variable, EditorVariableContent content, Node node, XPath xpath) throws XPathExpressionException {

        DataAttributes da = content.getAttrs();

        // set the attributes
        Map<String, String> attXpath = variable.getAttrsXPath();
        for (String att : attXpath.keySet()) {
            
            String relAttPath = attXpath.get(att).substring(variable.getDocumentXPath().length());
            if (relAttPath.startsWith("/")) {
                // remove leading / in e.g. /text()
                relAttPath = relAttPath.substring(1);
            }
            XPathExpression attExpr = xpath.compile(relAttPath);
            Node textNode = (Node) attExpr.evaluate(node, XPathConstants.NODE);
            textNode.setNodeValue(da.getAttribute(att));
        }
        
    }
    
    private String nodeToString(Node node){

      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer;
      String output = null;
      try {
          transformer = tf.newTransformer();
          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          StringWriter writer = new StringWriter();
          transformer.transform(new DOMSource(node), new StreamResult(writer));
          output = writer.getBuffer().toString();          
      } catch (TransformerConfigurationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (TransformerException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      
      return output;
        
    }
    
    private Document xmlToDocument(String xml){
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            System.out.println(xml);
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return document;
        
    }

    private List<XPathExpression> compileExpressions(Map<String, EditorVariable> vars, XPath xpath){
        
        List<XPathExpression> exps = new ArrayList<XPathExpression>();
        for( EditorVariable var : vars.values() ){
            
            try {
                exps.add(xpath.compile(var.getTemplateXPath()));
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        return exps;
        
    }

    public Map<String, EditorVariable> getTemplate() {
        return template;
    }


}
