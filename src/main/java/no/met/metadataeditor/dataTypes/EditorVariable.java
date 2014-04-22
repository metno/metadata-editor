package no.met.metadataeditor.dataTypes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import no.met.metadataeditor.dataTypes.attributes.DataAttribute;

public class EditorVariable {

    public final static String DEFAULT_RESOURCE = "default";

    private Map<String, URI> resources = new HashMap<>();
    private Map<String, String> attrsXPath = new HashMap<>();
    private String documentXPath = null;
    private String templateXPath;
    private DataAttribute dataAttributesType;
    private Map<String, EditorVariable> children = new HashMap<>();
    private int minOccurs = 1;
    private int maxOccurs = 1;

    private String selectionXPath;

    public EditorVariable() {

    }

    /**
     * initialize a new EditorVariable of the type defined by the DataAttributes
     *
     * @param dataAttributesType an empty Object indicating the type of data
     */
    public EditorVariable(DataAttribute dataAttributesType) {
        this.dataAttributesType = dataAttributesType;
    }

    public String getType() {
        return getClass().getSimpleName() + "::" + dataAttributesType.getClass().getSimpleName();
    }

    /**
     * Get a new instance of the attributes-class belonging to this
     * EditorVariable. Use this method to fill the EditorVariableContent
     * with Attributes
     * @return new instance of the dataAttributes
     * @see DataAttributes.addAttribute(String attr, String value)
     */
    public DataAttribute getNewDataAttributes() {
        return dataAttributesType.newInstance();
    }

    public Map<String, URI> getResources() {
        return resources;
    }

    public void addResource(String name, URI uri) {
        resources.put(name, uri);
    }

    public URI getDefaultResourceURI(){
        return getResourceURI(DEFAULT_RESOURCE);
    }

    public URI getResourceURI(String resourceName){
        return resources.get(resourceName);
    }

    /**
     *
     * @return minimum allowed occurences of content
     */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
     * Set the minimum allowed occurences of the content. If minOccurs > 0, this
     * is a required field.
     *
     * @param minOccurs
     */
    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
     *
     * @return the maximum allowed occurance of the content
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * Set the maximum allowed number of occurances of the content, default 1.
     * Use Integer.MAX_VALUE for unbound
     *
     * @param maxOccurs
     */
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    /**
     *
     * @return returns a new DataAttributes object of the type belonging to this
     *         EditorVariable
     */
    public DataAttribute getDataAttributes() {
        return dataAttributesType;
    }

    public Map<String, DataType> getAttrsType() {
        return getDataAttributes().getAttributesSetup();
    }

    public Map<String, EditorVariable> getChildren() {
        return children;
    }

    public void addChild(String varName, EditorVariable child) {
        children.put(varName, child);
    }

    public void setChildren(Map<String, EditorVariable> children){
        this.children = children;
    }

    /**
     * Get the attributes xpath directive, e.g. where to find
     * content in a existing document
     * @return
     */
    public Map<String, String> getAttrsXPath() {
        return attrsXPath;
    }

    void setAttrsXPath(String attribute, String xPath) {
        if (getAttrsType().containsKey(attribute)) {
            attrsXPath.put(attribute, xPath);
        } else {
            throw new AttributesMismatchException("cannot set xpath for attribute: "+attribute);
        }
    }

    public boolean attrsXPathValid(){

        for( String replaceVar : dataAttributesType.getAttributesSetup().keySet() ){
            if( !attrsXPath.containsKey(replaceVar)){
                return false;
            }
        }
        return true;
    }

    public String getDocumentXPath() {
        return documentXPath;
    }

    public void setDocumentXPath(String documentXPath) {
        this.documentXPath = documentXPath;
    }

    public String getTemplateXPath() {
        return templateXPath;
    }

    public void setTemplateXPath(String templateXPath) {
        this.templateXPath = templateXPath;
    }


    public boolean hasDefaultResource() {
        return resources.containsKey(DEFAULT_RESOURCE);
    }

    public String getSelectionXPath() {
        return selectionXPath;
    }

    public void setSelectionXPath(String selectionXPath) {
        this.selectionXPath = selectionXPath;
    }

}
