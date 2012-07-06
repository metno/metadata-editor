package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="uri")
public class UriWidget extends EditorWidget {

    /**
     * 
     */
    private static final long serialVersionUID = -1244778870767868773L;

    public UriWidget(){
        super();
    }
    
    public UriWidget(String label, String variableName) {
        super(label, variableName);
    }

    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("uri", "");
        return defaultValue;
    }


}
