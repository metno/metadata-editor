package no.met.metadataeditor.widget;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.datastore.DataStore;

/**
 * Class for representing widgets. This is a pure data class and should never
 * contain any logic.
 */
@XmlRootElement
@XmlSeeAlso({ LatLonBoundingBoxWidget.class, ListWidget.class, StartAndStopTimeWidget.class, StringWidget.class,
        UriWidget.class, TextAreaWidget.class })
public abstract class EditorWidget implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2532825684273483564L;

    // the variable name that is used for this field. Must correspond with the
    // same name in the template
    private String variableName;

    private String label;

    private List<Map<String, String>> values;

    private boolean isPopulated = false;

    private int maxOccurs = 1;

    private int minOccurs = 1;

    private List<String> resourceValues = new ArrayList<String>();
    
    private List<EditorWidget> children = new ArrayList<EditorWidget>();

    public EditorWidget() {
        values = new ArrayList<Map<String, String>>();
    }

    public EditorWidget(String label, String variableName) {
        this.label = label;
        this.variableName = variableName;

        values = new ArrayList<Map<String, String>>();
    }

    @XmlAttribute
    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @XmlAttribute
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public void addValue(Map<String, String> value) {
        values.add(value);
    }

    public void removeValue(Map<String, String> value) {
        values.remove(value);
    }

    public void configure(String project, DataStore dataStore, EditorVariable variable) {
        maxOccurs = variable.getMaxOccurs();
        minOccurs = variable.getMinOccurs();

        if (null != variable.getDefaultResourceURI()) {
            resourceValues = fetchResourceValues(variable.getDefaultResourceURI(), project, dataStore);
        }
    }

    public void populate(List<EditorVariableContent> contents) {

        for (EditorVariableContent content : contents) {
            DataAttributes attrs = content.getAttrs();

            Map<String, String> value = new HashMap<String, String>();
            List<String> relevantAttrs = getRelevantAttributes();
            for (String attr : relevantAttrs) {
                value.put(attr, attrs.getAttribute(attr));
            }

            addValue(value);
        }

        isPopulated = true;

    }

    /**
     * Take the information stored in the widget and send it back to the
     * EditorVariable
     */
    public List<EditorVariableContent> getContent(EditorVariable ev) {

        List<EditorVariableContent> contentList = new ArrayList<EditorVariableContent>();
        for (Map<String, String> value : values) {

            EditorVariableContent content = new EditorVariableContent();
            DataAttributes da = ev.getNewDataAttributes();
            content.setAttrs(da);
            for (Map.Entry<String, String> entry : value.entrySet()) {
                da.addAttribute(entry.getKey(), entry.getValue());
            }
            contentList.add(content);
        }

        return contentList;

    }

    private List<String> getRelevantAttributes() {

        Map<String, String> defaultValues = getDefaultValue();
        List<String> relevantAttributes = new ArrayList<String>();
        for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
            relevantAttributes.add(entry.getKey());
        }

        return relevantAttributes;

    }

    public void addNewValue() {
        addValue(getDefaultValue());
    }

    public String getWidgetType() {
        return getClass().getName();
    }

    private List<String> fetchResourceValues(URI resourceURI, String project, DataStore dataStore) {

        String resourceString = dataStore.readResource(project, resourceURI.toString());

        String[] resourceValues = resourceString.split("\n");
        return Arrays.asList(resourceValues);
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public boolean isPopulated() {
        return isPopulated;
    }

    public abstract Map<String, String> getDefaultValue();

    public List<String> getResourceValues() {
        return resourceValues;
    }

    public void addMissingOccurs() {
        
        while( values.size() < minOccurs ){
            addNewValue();
        }
        
    }

    @XmlElement
    public List<EditorWidget> getChildren() {
        return children;
    }

    public void setChildren(List<EditorWidget> children) {
        this.children = children;
    }

}
