package no.met.metadataeditor.widget;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import no.met.metadataeditor.EditorException;
import no.met.metadataeditor.EditorWidgetView;
import no.met.metadataeditor.InvalidEditorConfigurationException;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;

/**
 * Base class for widgets. Contains most of the logic for working with the widgets.
 */
@XmlRootElement
@XmlSeeAlso({ LatLonBoundingBoxWidget.class, ListWidget.class, StartAndStopTimeWidget.class, StringWidget.class,
        UriWidget.class, TextAreaWidget.class, TimeWidget.class, MultiSelectListWidget.class, StringAndListWidget.class,
        OnlineResourceWidget.class, ContainerWidget.class, KeyValueListWidget.class, DateWidget.class, XMDInfoWidget.class,
        XMDDisplayAreaWidget.class, XMDWmsInfoWidget.class, XMDProjectionDatasetWidget.class, XMDProjectionFimexWidget.class,
        AutoUUIDWidget.class, NowDateWidget.class, MetnoDatasetIdentifierWidget.class, SkosListWidget.class})
public abstract class EditorWidget implements Serializable {

    private static final long serialVersionUID = -2532825684273483564L;

    // the variable name that is used for this field. Must correspond with the
    // same name in the template
    private String variableName;

    private String label;

    private String description;

    private boolean isPopulated = false;

    private int maxOccurs = 1;

    private int minOccurs = 1;

    private URI resourceUri;

    private List<EditorWidget> children = new ArrayList<EditorWidget>();

    private List<EditorWidgetView> widgetViews = new ArrayList<EditorWidgetView>();

    private Class<? extends DataAttribute> attributeClass;

    public EditorWidget() {

    }


    public EditorWidget(EditorWidget cloneFrom){

        this.variableName = cloneFrom.variableName;
        this.label = cloneFrom.label;
        this.description = cloneFrom.description;
        this.maxOccurs = cloneFrom.maxOccurs;
        this.minOccurs = cloneFrom.minOccurs;
        this.resourceUri = cloneFrom.resourceUri;
        this.attributeClass = cloneFrom.attributeClass;
        this.children = new ArrayList<EditorWidget>();
        Collections.copy(this.children, cloneFrom.children);

    }

    public EditorWidget(String label, String variableName) {
        this.label = label;
        this.variableName = variableName;

    }

    @XmlAttribute
    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @XmlElement(namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @XmlAttribute
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public void setMinOccurs(int minOccurs){
        this.minOccurs = minOccurs;
    }
    
    public void setMaxOccurs(int maxOccurs){
        this.maxOccurs = maxOccurs;
    }

    public Class<? extends DataAttribute> getAttributeClass() {
        return attributeClass;
    }


    public void setAttributeClass(Class<? extends DataAttribute> attributeClass) {
        this.attributeClass = attributeClass;
    }


    /**
     * Add configuration from the editor variable to the widget and the widgets children.
     * @param variable
     * @throws InvalidEditorConfigurationException Thrown if a child widget refers to a variable not
     * found in the child variables or the widget configuration is not valid.
     * @return Always returns true
     */
    public boolean configure(EditorVariable variable) {
        maxOccurs = variable.getMaxOccurs();
        minOccurs = variable.getMinOccurs();

        attributeClass = variable.getDataAttributes().getClass();

        setResourceUri(variable.getDefaultResourceURI());

        Map<String,EditorVariable> childVarMap = variable.getChildren();
        for( EditorWidget child : children ){

            String varName = child.getVariableName();
            if( !childVarMap.containsKey(varName)){
                throw new InvalidEditorConfigurationException( varName + " is not found in the template." );
            }

            EditorVariable ev = childVarMap.get(varName);
            child.configure(ev);
        }
        
        // throws
        validateConfiguration();

        return true;
    }
    
    /**
     * Validate that the configuration of the widget is valid or not. The base class implementation never
     * throws an exception.
     * @throws InvalidEditorConfigurationException If the configuration is not valid it will throw an exception.
     */
    protected void validateConfiguration() {
        // do nothing in base class
    }


    public void generateWidgetViews(List<EditorVariableContent> contents){


        for (EditorVariableContent content : contents) {

            DataAttribute attrs = content.getAttrs();
            Map<String,String> values = attrs.getAttributes(getRelevantAttributes().toArray(new String[0]));
            EditorWidgetView view = createWidgetView(values);
            widgetViews.add(view);

            // recursively populate all children
            Map<String, List<EditorVariableContent>> childContentMap = content.getChildren();
            for( Map.Entry<String, List<EditorVariableContent>> entry : childContentMap.entrySet() ){

                String varName = entry.getKey();
                List<EditorVariableContent> childContent = entry.getValue();

                if( view.hasChildWidget(varName) ){
                    EditorWidget childWidget = view.getChildWidget(varName);
                    childWidget.generateWidgetViews(childContent);
                }
            }
        }

        // add extra widget views to ensure that we have as many as minOccurs demands
        while( widgetViews.size() < minOccurs ){
            addNewValue();
        }

        isPopulated = true;

    }

    EditorWidgetView createWidgetView(Map<String, String> values){

        EditorWidgetView view = new EditorWidgetView();

        List<EditorWidget> viewChildWidgets = new ArrayList<EditorWidget>();
        for( EditorWidget child : children ){
            viewChildWidgets.add(cloneInstance(child));
        }
        view.setChildren(viewChildWidgets);
        view.setValues(values);
        view.setDataAttributeClass(attributeClass);
        return view;
    }


    /**
     * Take the information stored in the widget and send it back to the
     * EditorVariable
     */
    public List<EditorVariableContent> getContent() {

        List<EditorVariableContent> contentList = new ArrayList<EditorVariableContent>();
        for( EditorWidgetView view : widgetViews ){

            EditorVariableContent content = getContentForView(view);
            contentList.add(content);

            // recursively get content from child widgets.
            Map<String, List<EditorVariableContent>> childContentMap = new HashMap<String,List<EditorVariableContent>>();
            for( EditorWidget child : children){
                String varName = child.getVariableName();

                if( view.hasChildWidget(varName)) {
                    EditorWidget childWidget = view.getChildWidget(varName);
                    List<EditorVariableContent> childContent = childWidget.getContent();
                    childContentMap.put(varName, childContent);
                }
            }            
            content.setChildren(childContentMap);
        }

        return contentList;

    }
    
    protected EditorVariableContent getContentForView(EditorWidgetView view){
        
        EditorVariableContent content = new EditorVariableContent();
        DataAttribute da = view.valuesAsAttriubte();
        content.setAttrs(da);
        return content;
        
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

        EditorWidgetView view = createWidgetView(getDefaultValue());
        widgetViews.add(view);

    }

    public String getWidgetType() {
        return getClass().getName();
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }

    public int getMinOccurs() {
        return minOccurs;
    }

    public URI getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(URI resourceUri) {
        this.resourceUri = resourceUri;
    }

    public boolean isPopulated() {
        return isPopulated;
    }

    public abstract Map<String, String> getDefaultValue();


    @XmlElement(name="widget", namespace="http://www.met.no/schema/metadataeditor/editorConfiguration")
    public List<EditorWidget> getChildren() {
        return children;
    }

    public void setChildren(List<EditorWidget> children) {
        this.children = children;
    }

    /**
     * Convert the widget tree under this editor widget to a list.
     * @return
     */
    public List<EditorWidget> getWidgetTreeAsList() {

        List<EditorWidget> widgetTree = new ArrayList<EditorWidget>();
        for( EditorWidget child : children ){
            widgetTree.add(child);
            widgetTree.addAll(child.getWidgetTreeAsList());
        }
        return widgetTree;
    }

    public List<EditorWidgetView> getWidgetViews() {
        return widgetViews;
    }

    public void setWidgetViews(List<EditorWidgetView> widgetViews) {
        this.widgetViews = widgetViews;
    }

    /**
     * Clone an instance of an EditorWidget. Will clone correctly for subclasses as well.
     * @param cloneFrom
     * @return
     */
    private EditorWidget cloneInstance(EditorWidget cloneFrom){

        Class<? extends EditorWidget> cls = cloneFrom.getClass();

        try {
            Constructor<EditorWidget> ctr = getCopyConstructor(cls);
            EditorWidget widget = ctr.newInstance(cloneFrom);
            return widget;

        } catch (SecurityException e) {
            String msg = "No access to copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        } catch (InstantiationException e) {
            String msg = "Failed to execute copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "No access to copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg = "Illegal argument to copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        } catch (InvocationTargetException e) {
            String msg = "Wrong invocation target for copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        }

    }

    private Constructor<EditorWidget> getCopyConstructor(Class<? extends EditorWidget> cls){

        try {
            @SuppressWarnings("unchecked")
            Constructor<EditorWidget> ctr = (Constructor<EditorWidget>) cls.getConstructor(cls);
            return ctr;
        } catch (NoSuchMethodException e) {
            String msg = "No copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);

        } catch (SecurityException e) {
            String msg = "No access to copy constructor for class: " + cls.getName();
            throw new EditorException(msg, e);
        }

    }


    public void removeValue(EditorWidgetView widgetView) {
        widgetViews.remove(widgetView);
    }


}
