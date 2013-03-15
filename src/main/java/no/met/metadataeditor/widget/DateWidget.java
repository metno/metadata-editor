package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget used to display a date selector.
 */
public class DateWidget extends EditorWidget {

    private static final long serialVersionUID = 8298634817093453102L;

    public DateWidget(){
        super();
    }

    public DateWidget(DateWidget cloneFrom){
        super(cloneFrom);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("date", "");
        return defaultValue;
    }

}
