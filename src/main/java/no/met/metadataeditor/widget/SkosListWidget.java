
package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;

public class SkosListWidget extends EditorWidget {
    private static final long serialVersionUID = -7763892117237980221L;
    private String currentValue = "Search keywords";
    private int size = 80;
    private int maxlength = Integer.MAX_VALUE;

    public SkosListWidget() {
    }

    public SkosListWidget(SkosListWidget cloneFrom) {
        super(cloneFrom);
        this.size = cloneFrom.getSize();
        this.maxlength = cloneFrom.getMaxlength();
    }    

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<>();
        defaultValue.put("listElement", currentValue);         
        return defaultValue;
    }   

    @XmlAttribute
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @XmlAttribute
    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }
        
}
