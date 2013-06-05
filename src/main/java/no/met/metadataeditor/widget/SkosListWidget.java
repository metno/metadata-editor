
package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class SkosListWidget extends EditorWidget {
    private static final long serialVersionUID = -7763892117237980221L;

    public SkosListWidget() {
    }

    public SkosListWidget(SkosListWidget cloneFrom) {
        super(cloneFrom);
    }    
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<>();
        defaultValue.put("listElement", "");    
        return defaultValue;
    }
        
}
