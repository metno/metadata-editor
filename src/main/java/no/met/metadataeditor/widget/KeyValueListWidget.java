package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget used for showing a list of values and also storing the associated key.
 * 
 * The widget assumes resource files where the key value pairs are stored in a file and separated by
 * new line.
 */
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
        
        Map<String,String> defaultValue = new HashMap<>();
        defaultValue.put("key", ""); 
        defaultValue.put("value", "");
        return defaultValue;
    }

}
