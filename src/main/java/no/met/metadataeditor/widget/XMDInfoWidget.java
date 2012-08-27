package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class XMDInfoWidget extends EditorWidget {

    private static final long serialVersionUID = -2328181162179533471L;

    public XMDInfoWidget(){
        super();
    }

    public XMDInfoWidget(XMDInfoWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("name", "");
        defaultValue.put("status", "");
        defaultValue.put("creation", "");
        defaultValue.put("lastChanged", "");
        defaultValue.put("ownertag", "");

        return defaultValue;
    }

}
