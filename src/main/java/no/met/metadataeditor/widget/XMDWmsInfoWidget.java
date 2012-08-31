package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class XMDWmsInfoWidget extends EditorWidget {

    private static final long serialVersionUID = 4538571837795263839L;

    public XMDWmsInfoWidget(){
        super();
    }

    public XMDWmsInfoWidget(XMDWmsInfoWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("uri", "");
        return defaultValue;
    }

}
