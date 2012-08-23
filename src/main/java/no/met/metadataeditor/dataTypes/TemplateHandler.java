package no.met.metadataeditor.dataTypes;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import no.met.metadataeditor.dataTypes.attributes.ContainerAttribute;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.dataTypes.attributes.KeyValueListAttribute;
import no.met.metadataeditor.dataTypes.attributes.LatLonBBAttribute;
import no.met.metadataeditor.dataTypes.attributes.LatLonBBSingleAttribute;
import no.met.metadataeditor.dataTypes.attributes.ListElementAttribute;
import no.met.metadataeditor.dataTypes.attributes.StartAndStopTimeAttribute;
import no.met.metadataeditor.dataTypes.attributes.StringAndListElementAttribute;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;
import no.met.metadataeditor.dataTypes.attributes.TimeAttribute;
import no.met.metadataeditor.dataTypes.attributes.UriAttribute;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

class TemplateHandler extends DefaultHandler {

    final String EDT = "http://www.met.no/schema/metadataeditor/editorDataTypes";
    Deque<EditorVariable> edtElements;
    Deque<String> fullPathElements;
    Deque<String> finalPathElements; // without EDT-elements
    // xpath to find an EditorVariable-attribute
    Map<String, Deque<EditorVariable>> attributeXPath;
    private Map<String, EditorVariable> resultConfig;
    StringBuffer elementContent;
    private Map<String, String> namespacePrefixes = new HashMap<String,String>();

    // mapping between namespace names like 'gmd' => 'ns1'. Used to fix XPath expressions hardcoded
    // in the templates
    private Map<String, String> prefixMapping = new HashMap<String,String>();

    @Override
    public void startDocument() throws SAXException {
        edtElements  = new ArrayDeque<EditorVariable>();

        fullPathElements = new ArrayDeque<String>();
        finalPathElements = new ArrayDeque<String>();
        resultConfig = null;
        attributeXPath = new HashMap<String, Deque<EditorVariable>>();
        elementContent = new StringBuffer();

    }

    private String variableAddAttributes(EditorVariable ev, Attributes atts) throws SAXException {
        ev.setMaxOccurs(getMaxOccurs(atts));
        ev.setMinOccurs(getMinOccurs(atts));

        ev.setSelectionXPath(getXPath(atts));

        String res = atts.getValue("resource");
        if (res != null) {
            URI uri;
            try {
                uri = new URI(res);
            } catch (URISyntaxException e) {
                throw new SAXNotRecognizedException(e.toString());
            }
            ev.addResource(EditorVariable.DEFAULT_RESOURCE, uri);
        }

        return atts.getValue("varName");
    }

    private String getXPath(Attributes atts){

        String xpath = atts.getValue("xpath");
        if( xpath == null ){
            return null;
        }

        for( Map.Entry<String, String> entry : prefixMapping.entrySet() ){

            String oldNamespace = entry.getKey();
            String newNamespace = entry.getValue();
            xpath = xpath.replace(oldNamespace + ":", newNamespace + ":");
        }
        return xpath;

    }

    private static int getMinOccurs(Attributes atts){

        String minOccurs = atts.getValue("minOccurs");

        if(null == minOccurs){
            return 1;
        }

        if(0 == minOccurs.length()){
            throw new InvalidTemplateException("minOccurs was an empty string by needs to be a number.");
        }

        int minValue;
        try {
            minValue = Integer.parseInt(minOccurs);
        } catch(NumberFormatException e){
            throw new InvalidTemplateException(e.getMessage());
        }

        if( minValue < 0 ){
            throw new InvalidTemplateException("minOccurs needs to 0 or larger");
        }

        return minValue;
    }

    private static int getMaxOccurs(Attributes atts){

        String maxOccurs = atts.getValue("maxOccurs");
        if (maxOccurs == null) {
            return 1;
        }else if ("unbounded".equals(maxOccurs)) {
            return Integer.MAX_VALUE;
        } else {

            int maxValue;
            try {
                maxValue = Integer.parseInt(maxOccurs);
            } catch(NumberFormatException e){
                throw new InvalidTemplateException(e.getMessage());
            }

            if( maxValue < 1 ){
                throw new InvalidTemplateException("maxOccurs needs to be larger than 0");
            }

            return maxValue;
        }
    }

    private void addStandardEDT(DataAttribute da, String nsUri, String lName, Attributes atts) throws SAXException {
        EditorVariable ev = new EditorVariable(da);
        String varName = variableAddAttributes(ev, atts);
        fullPathElements.addLast(getTemplateQName(nsUri, String.format("%s[@varName='%s']",lName, varName)));

        ev.setTemplateXPath(StringUtils.join(fullPathElements.iterator(), "/"));
        edtElements.getLast().addChild(varName, ev);
        edtElements.addLast(ev);

        for (String key : ev.getAttrsType().keySet()) {
            if (! attributeXPath.containsKey(key)) {
                attributeXPath.put(key, new ArrayDeque<EditorVariable>());
            }
            attributeXPath.get(key).addLast(ev);
        }
    }

    /**
     * check if one of the EditorVariable Attributes are placed in an attribute
     * @param atts
     */
    private void searchEditorAttributes(Attributes atts) {
        Set<String> keys = new HashSet<String>(attributeXPath.keySet());
        for (String key : keys) {
            Pattern keyPattern = Pattern.compile("\\s*\\Q$"+key+"\\E\\s*");
            for (int i = 0; i < atts.getLength(); ++i) {
                if (keyPattern.matcher(atts.getValue(i)).matches()) {
                    String xPath = org.apache.commons.lang3.StringUtils.join(finalPathElements.iterator(), "/");
                    xPath += "/@"+getTemplateQName(atts.getURI(i), atts.getLocalName(i));
                    Deque<EditorVariable> evs = attributeXPath.get(key);
                    EditorVariable ev = evs.removeLast();
                    ev.setAttrsXPath(key, xPath);
                    if (evs.size() == 0) {
                        attributeXPath.remove(key);
                    }
                }
            }
        }
    }

    private void searchEditorAttributesInContent() {
        Set<String> keys = new HashSet<String>(attributeXPath.keySet());
        for (String key : keys) {
            Pattern keyPattern = Pattern.compile("\\s*\\Q$"+key+"\\E\\s*");
            if (keyPattern.matcher(elementContent).matches()) {
                String xPath = org.apache.commons.lang3.StringUtils.join(finalPathElements.iterator(), "/");
                xPath += "/text()";
                Deque<EditorVariable> evs = attributeXPath.get(key);
                EditorVariable ev = evs.removeLast();
                ev.setAttrsXPath(key, xPath);
                if (evs.size() == 0) {
                    attributeXPath.remove(key);
                }
            }
        }
    }

    private String getTemplateQName(String nsUri, String lName) {

        if(nsUri == null || "".equals(nsUri)){
            return lName;
        }

        String prefix = namespacePrefixes.get(nsUri);
        if (prefix == null) {
            prefix = "ns" + namespacePrefixes.size();
            namespacePrefixes.put(nsUri, prefix);
        }
        return String.format("%s:%s", prefix, lName);
    }

    // generate a xpath identifier with name and attributes
    private String generateXPathIdentifier(String nsUri, String lName, String qName, Attributes atts) {
        String xpathPart = getTemplateQName(nsUri, lName);
        List<String> attrs = new ArrayList<String>();
        for (int i = 0; i < atts.getLength(); ++i) {
            // only use local attributes
            if ("".equals(atts.getURI(i)) ||
                    atts.getURI(i).equals(nsUri)) {
                // don't use local attributes containing EditorAttributes wildcards
                if (!atts.getValue(i).matches("\\s*\\$.*")) {
                    attrs.add(String.format("@%s='%s'", getTemplateQName(atts.getURI(i), atts.getLocalName(i)), atts.getValue(i)));
                }
            }
        }
        if (attrs.size() > 0) {
            xpathPart = String.format("%s[%s]", xpathPart, StringUtils.join(attrs.iterator(), " and "));
        }
        return xpathPart;
    }

    @Override
    public void startElement(String nsUri, String lName, String qName, Attributes atts) throws SAXException {
        if (EDT.equals(nsUri)) {
            if ("editorDataTypes".equals(lName)) {
                //  start element, empty container
                assert(edtElements.size() == 0);
                EditorVariable ev = new EditorVariable(new ContainerAttribute());
                edtElements.addLast(ev);
                fullPathElements.addLast(getTemplateQName(nsUri, lName));
            } else if ("container".equals(lName)) {
                addStandardEDT(new ContainerAttribute(), nsUri, lName, atts);
            } else if ("lonLatBoundingBox".equals(lName)) {
                addStandardEDT(new LatLonBBAttribute(), nsUri, lName, atts);
            } else if ("lonLatBoundingBoxSingle".equals(lName)) {
                addStandardEDT(new LatLonBBSingleAttribute(), nsUri, lName, atts);
            } else if ("string".equals(lName)) {
                addStandardEDT(new StringAttribute(), nsUri, lName, atts);
            } else if ("uri".equals(lName)) {
                addStandardEDT(new UriAttribute(), nsUri, lName, atts);
            } else if ("list".equals(lName)) {
                addStandardEDT(new ListElementAttribute(), nsUri, lName, atts);
            } else if ("stringAndList".equals(lName)) {
                addStandardEDT(new StringAndListElementAttribute(), nsUri, lName, atts);
            } else if ("startAndStopTime".equals(lName)) {
                addStandardEDT(new StartAndStopTimeAttribute(), nsUri, lName, atts);
            } else if ("time".equals(lName)) {
                addStandardEDT(new TimeAttribute(), nsUri, lName, atts);
            } else if ("keyValueList".equals(lName)) {
                addStandardEDT(new KeyValueListAttribute(), nsUri, lName, atts);
            } else {
                throw new UndefinedEditorVariableException(lName);
            }
        } else {
            fullPathElements.addLast(getTemplateQName(nsUri, lName));
            finalPathElements.addLast(generateXPathIdentifier(nsUri, lName, qName, atts));
            searchEditorAttributes(atts);
            String documentXPath = StringUtils.join(finalPathElements.iterator(), "/");
            for (EditorVariable ev : edtElements) {
                if (ev.getDocumentXPath() == null) {
                    ev.setDocumentXPath(documentXPath);
                }
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String nsUri, String lName, String qName) {
        searchEditorAttributesInContent();
        elementContent.setLength(0); // clear
        fullPathElements.removeLast();
        if (EDT.equals(nsUri)) {
            if ("editorDataTypes".equals(lName)) {
                assert(edtElements.size() == 1);
                resultConfig = edtElements.getFirst().getChildren();
                validateConfig(resultConfig, "");
            }
            edtElements.removeLast();
        } else {
            finalPathElements.removeLast();
        }
    }

    public Map<String, EditorVariable> getResultConfig() {
        return resultConfig;
    }

    public Map<String, String> getNamespacePrefixes() {
        return namespacePrefixes;
    }

    public void setNamespacePrefixes(Map<String,String> namespacePrefixes){

        this.namespacePrefixes.clear();
        this.prefixMapping.clear();

        for(Map.Entry<String, String> entry : namespacePrefixes.entrySet()){
            String prefix = "ns" + this.namespacePrefixes.size();
            this.namespacePrefixes.put(entry.getKey(), prefix);
            this.prefixMapping.put(entry.getValue(), prefix);
        }

    }

    public void validateConfig(Map<String, EditorVariable> config, String namespace){

        for( Map.Entry<String, EditorVariable> entry : config.entrySet()){

            EditorVariable ev = entry.getValue();
            if( !ev.attrsXPathValid() ){
                throw new InvalidTemplateException("One or $<varname> attributes are missing for variable: " + namespace + entry.getKey());
            }

            validateConfig(ev.getChildren(), entry.getKey() + "::");
        }

    }

}
