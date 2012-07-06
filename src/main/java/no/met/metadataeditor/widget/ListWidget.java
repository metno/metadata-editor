package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

public class ListWidget extends EditorWidget {

    private static final long serialVersionUID = 5670896760766742704L;

    public ListWidget(){
        super();
    }
    
    public ListWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("listElement", "");    
        return defaultValue;
    }
    
}
