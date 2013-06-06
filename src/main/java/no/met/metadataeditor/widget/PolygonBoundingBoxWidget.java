package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget used for editing polygon bounding boxes
 */
public class PolygonBoundingBoxWidget extends EditorWidget {

    private static final long serialVersionUID = -8780455486351248677L;

    public PolygonBoundingBoxWidget() {
        super();
    }
    
    public PolygonBoundingBoxWidget(PolygonBoundingBoxWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("str", "");
        return defaultValue;
    }


}
