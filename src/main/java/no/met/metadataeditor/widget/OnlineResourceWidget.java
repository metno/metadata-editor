package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

public class OnlineResourceWidget extends EditorWidget {

    private static final long serialVersionUID = 1494453139478209709L;

    public OnlineResourceWidget(){
        super();
    }
    
    public OnlineResourceWidget(OnlineResourceWidget cloneFrom){
        super(cloneFrom);
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        return new HashMap<>();
    }

}
