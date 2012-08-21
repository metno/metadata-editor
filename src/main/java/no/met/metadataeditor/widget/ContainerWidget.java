package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Empty widget just used to contain other widgets
 */
public class ContainerWidget extends EditorWidget {

    private static final long serialVersionUID = -2410666893171644122L;
    
    public ContainerWidget(){
        super();
    }
    
    public ContainerWidget(ContainerWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        return defaultValue;
    }

}
