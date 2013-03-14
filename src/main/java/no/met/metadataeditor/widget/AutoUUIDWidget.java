package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoUUIDWidget extends EditorWidget {

    private static final long serialVersionUID = -2261929414552684579L;

    public AutoUUIDWidget(){
        super();
    }

    public AutoUUIDWidget(AutoUUIDWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<String,String>();
        String uuid = UUID.randomUUID().toString();
        defaultValue.put("str", uuid);
        return defaultValue;
    }    

}
