package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class ListWidget extends EditorWidget {

    private static final long serialVersionUID = 5670896760766742704L;

    public ListWidget(){
        super();
    }
    
    public ListWidget(ListWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<>();
        defaultValue.put("listElement", "");    
        return defaultValue;
    }
    
}
