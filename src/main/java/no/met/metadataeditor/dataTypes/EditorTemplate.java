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
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.istack.logging.Logger;

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

        SAXBuilder builder = new SAXBuilder();
        TemplateRootNode root = null;
        try {
            org.jdom2.Document d = builder.build(templateXML);
            root = genTemplateTree(d, content);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        org.jdom2.Document doc = replaceVars(root);
        pruneTree(doc);        

        
        return doc;
        
    }       
    
    private void pruneTree(org.jdom2.Document doc){
        
        pruneTreeRecursive(doc.getRootElement(), null, doc);
        
    }
    
    private List<Content> pruneTreeRecursive(Element element, Element parent, org.jdom2.Document doc){
       
        List<Content> children = new ArrayList<Content>();
        for( Content child : element.getContent()){
            children.add(child);
        }        
       
        if( templateTags.containsKey(element.getName())){
            
            element.detach();
            for( Content child : children ){

                if( child instanceof Element ){
                    Element e = (Element) child;
                    pruneTreeRecursive(e, parent, doc);                    
                }

                child.detach();
                if( parent != null ){
                    parent.addContent(child);
                } else {
                    doc.addContent(child);
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
        return null;
    }
    
    private TemplateRootNode genTemplateTree(org.jdom2.Document templateDoc, Map<String, List<EditorVariableContent>> content) {

        // skip the top node since it is part of the editor template and not the XML we want
        // in the end.
        Element templateRoot = templateDoc.getRootElement().getChildren().get(0);        
        
        TemplateRootNode trn = new TemplateRootNode();
        trn.children = genTemplateTreeRecursive(templateRoot, content);
        trn.xmlNode = templateRoot;
        
        return trn;
    }
    
    private List<TemplateNode> genTemplateTreeRecursive(Element element, Map<String, List<EditorVariableContent>> contentMap){
               
        List<TemplateNode> children = new ArrayList<TemplateNode>();

        for( Content c : element.getContent() ){
            
            if( c instanceof Element ){
                Element child = (Element) c;
                if( templateTags.containsKey(child.getName()) ){
                                                    
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
    
        
    public Map<String, EditorVariable> getTemplate() {
        return template;
    }


}
