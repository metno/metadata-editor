package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class TimeWidget extends EditorWidget {

    private static final long serialVersionUID = -3555902527146582233L;
    
    public TimeWidget(){
        super();
    }
    
    public TimeWidget(TimeWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("time", "");
        return defaultValue;
    }

}
