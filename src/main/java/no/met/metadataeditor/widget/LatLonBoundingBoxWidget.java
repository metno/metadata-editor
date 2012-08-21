package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class LatLonBoundingBoxWidget extends EditorWidget {

    private static final long serialVersionUID = 8770219744361367595L;

    public LatLonBoundingBoxWidget(){
        super();
    }
    
    public LatLonBoundingBoxWidget(LatLonBoundingBoxWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String,String> getDefaultValue() {
        
        Map<String,String> value = new HashMap<String,String>();
        value.put("south", "0.0");
        value.put("north", "0.0");
        value.put("west", "0.0");
        value.put("east", "0.0");
        return value;
    }
        
}
