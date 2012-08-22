package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class KeyValueListWidget extends EditorWidget {

    private static final long serialVersionUID = 237486986900550983L;

    public KeyValueListWidget(){
        super();
    }
    
    public KeyValueListWidget(KeyValueListWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("key", ""); 
        defaultValue.put("value", "");
        return defaultValue;
    }

}
