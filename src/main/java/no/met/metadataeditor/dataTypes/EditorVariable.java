package no.met.metadataeditor.dataTypes;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class EditorVariable {
    private Map<String, URI> resources = new HashMap<String, URI>();
    private DataAttributes dataAttributesType;
    private Map<String, EditorVariable> children = new HashMap<String, EditorVariable>();
    private int minOccurs = 1;
    private int maxOccurs = 1;
    private List<EditorVariableContent> content = new ArrayList<EditorVariableContent>();

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

    public Map<String, URI> getResources() {
        return resources;
    }

    public void addResource(String name, URI uri) {
        resources.put(name, uri);
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
    @XmlTransient
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

}
