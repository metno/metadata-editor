package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

public class UriWidget extends EditorWidget {

    private static final long serialVersionUID = -1244778870767868773L;

    private int size = 100;
    
    private int maxlength = Integer.MAX_VALUE; 
    
    public UriWidget(){
        super();
    }
    
    public UriWidget(UriWidget cloneFrom){
        super(cloneFrom);
        
        this.size = cloneFrom.size;
        this.maxlength = cloneFrom.maxlength;        
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("uri", "");
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
