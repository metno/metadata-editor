package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class XMDProjectionFimexWidget extends EditorWidget {

    private static final long serialVersionUID = 2328877722573065407L;

    public XMDProjectionFimexWidget(){
        super();
    }

    public XMDProjectionFimexWidget(XMDProjectionFimexWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {

        Map<String,String> defaultValue = new HashMap<String, String>();
        defaultValue.put("name", "");
        defaultValue.put("method", "");
        defaultValue.put("projString", "");
        defaultValue.put("xAxis", "");
        defaultValue.put("yAxis", "");
        defaultValue.put("toDegree", "");
        return defaultValue;

    }

}
