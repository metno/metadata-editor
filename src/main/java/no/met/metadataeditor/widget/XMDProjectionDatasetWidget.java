package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class XMDProjectionDatasetWidget extends EditorWidget {

    private static final long serialVersionUID = -3355539599079243606L;

    public XMDProjectionDatasetWidget(){
        super();
    }

    public XMDProjectionDatasetWidget(XMDProjectionDatasetWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("urlRegex", "");
        defaultValue.put("urlReplace", "");
        return defaultValue;
    }

}
