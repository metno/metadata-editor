package no.met.metadataeditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.met.metadataeditor.dataTypes.DataAttributeValidationResult;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.widget.EditorWidget;

/**
 * Class used to represent one set of values for EditorWidget.
 * 
 * EditorWidgets can have several different values. Each set of value is represented by one
 * instance of this class.
 * 
 * The widget views are exposed to the JSF .xhtml files and used to fetch the values displayed
 * to the user.
 */
public class EditorWidgetView implements Serializable {

    private static final long serialVersionUID = 4577883892935459131L;

    private Map<String, String> values = new HashMap<String, String>();

    private List<EditorWidget> children = new ArrayList<EditorWidget>();

    private Map<String, EditorWidget> childMap = new HashMap<String,EditorWidget>();

    private Class<? extends DataAttribute> dataAttributeClass;

    public EditorWidgetView(){

    }

    public void addValue(String varName, String value) {
        values.put(varName, value);
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public List<EditorWidget> getChildren() {
        return children;
    }

    public void setChildren(List<EditorWidget> children) {
        this.children = children;

        childMap.clear();
        for( EditorWidget child : this.children ){
            childMap.put(child.getVariableName(), child);
        }

    }

    public boolean hasChildWidget(String varName){
        return childMap.containsKey(varName);
    }

    public EditorWidget getChildWidget(String varName){
        return childMap.get(varName);
    }

    public void setDataAttributeClass(Class<? extends DataAttribute> dataAttributeClass) {
        this.dataAttributeClass = dataAttributeClass;
    }

    public DataAttribute valuesAsAttriubte(){

        DataAttribute da = getAttributeInstance();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value =  entry.getValue();

            if( value != null ){
                value = value.replace("\r", "");
            }
            da.addAttribute(entry.getKey(), value);
        }
        return da;
    }

    private Map<String, DataAttributeValidationResult> validate(){

        DataAttribute da = valuesAsAttriubte();
        return da.validateAttributes();
    }

    public String validateAttribute(String attributeName){
        Map<String, DataAttributeValidationResult> result = validate();

        return result.get(attributeName).errorMsg;

    }

    private DataAttribute getAttributeInstance(){

        try {
            DataAttribute da = this.dataAttributeClass.newInstance();
            return da;
        } catch (InstantiationException e) {
            String msg = "Failed to instantiate a new DataAttribute instance in EditorWidgetView.";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg, e);
            throw new EditorException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Could not access constructed when creating a new DataAttribute instance in EditorWidgetView.";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, msg, e);
            throw new EditorException(msg, e);
        }


    }
}
