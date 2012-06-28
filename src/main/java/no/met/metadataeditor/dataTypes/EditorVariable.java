package no.met.metadataeditor.dataTypes;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EditorVariable implements Serializable {
    
    private static final long serialVersionUID = -43000469274325594L;

    public final static String DEFAULT_RESOURCE = "default";
    
    private Map<String, URI> resources = new HashMap<String, URI>();
    private Map<String, String> attrsXPath = new HashMap<String, String>();
    private String documentXPath = null;
    private String templateXPath;
    private DataAttributes dataAttributesType;
    private Map<String, EditorVariable> children = new HashMap<String, EditorVariable>();
    private int minOccurs = 1;
    private int maxOccurs = 1;

    private List<EditorVariableContent> content = new ArrayList<EditorVariableContent>();

    public EditorVariable() {

    }

    /**
     * initialize a new EditorVariable of the type defined by the DataAttributes
     *
     * @param dataAttributesType an empty Object indicating the type of data
     */
    public EditorVariable(DataAttributes dataAttributesType) {
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
    @JsonIgnore
    public DataAttributes getNewDataAttributes() {
        return dataAttributesType.newInstance();
    }

    public Map<String, URI> getResources() {
        return resources;
    }

    public void addResource(String name, URI uri) {
        resources.put(name, uri);
    }
    
    /**
     * Fetch a list of values from an associated resource.
     * @param name The given to the name of the resource when it was added. 
     * @return A list of values from the resource.
     */
    @JsonIgnore
    public List<String> getResourceValues(String name){
    
        if(!(resources.containsKey(name))){
            throw new IllegalArgumentException(name + " is not a valid resource name");
        }
        
        List<String> values = new ArrayList<String>();
        values.add("Africa");
        values.add("Europa");
        values.add("North America");
        values.add("y_wind");
        
        return values;
        
    }
    
    /**
     * Fetch the resource values for the default resource.
     * @return A list of values from the default resource.
     */
    @JsonIgnore
    public List<String> getDefaultResourceValues(){
        return getResourceValues(DEFAULT_RESOURCE);
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
    @JsonIgnore
    public DataAttributes getDataAttributes() {
        return dataAttributesType;
    }

    public Map<String, DataType> getAttrsType() {
        return getDataAttributes().getFields();
    }

    public Map<String, EditorVariable> getChildren() {
        return children;
    }

    public void addChild(String varName, EditorVariable child) {
        children.put(varName, child);
    }

    /**
     * Add content-data to the variable. Make sure the content-attributes
     * are already defined, otherwise a runtime-exception will be thrown.
     * @param content
     */
    public void addContent(EditorVariableContent content) {
        if (content.getAttrs().getClass().isInstance(getDataAttributes())) {
            this.content.add(content);
        } else {
            throw new AttributesMismatchException(content.getAttrs().getClass().getSimpleName() + " != " + getDataAttributes().getClass().getSimpleName());
        }
    }

    public List<EditorVariableContent> getContent() {
        return content;
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

}
