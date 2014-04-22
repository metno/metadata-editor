package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class XMDDisplayAreaWidget extends EditorWidget {

    private static final long serialVersionUID = -2261929414552684579L;

    public XMDDisplayAreaWidget(){
        super();
    }

    public XMDDisplayAreaWidget(XMDDisplayAreaWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<>();
        defaultValue.put("crs", "");
        defaultValue.put("left", "");
        defaultValue.put("right", "");
        defaultValue.put("top", "");
        defaultValue.put("bottom", "");
        return defaultValue;
    }

}
