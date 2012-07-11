package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

public class TextAreaWidget extends EditorWidget {

    private static final long serialVersionUID = 5150865263452703321L;
    
    private int cols;
    
    private int rows;
    

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("str", "");
        return defaultValue;
    }

    @XmlAttribute
    public int getCols() {
        return cols;
    }


    public void setCols(int cols) {
        this.cols = cols;
    }

    @XmlAttribute
    public int getRows() {
        return rows;
    }


    public void setRows(int rows) {
        this.rows = rows;
    }

}
