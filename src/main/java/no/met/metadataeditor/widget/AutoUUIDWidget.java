package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import no.met.metadataeditor.InvalidEditorConfigurationException;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

/**
 * Widget for auto generating a UUID value if a value is not found.
 * 
 * The widget only works with string variable with a max and min occurs of 1.
 */
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
    
    @Override
    public void validateConfiguration() {
        
        if( getMaxOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("maxOccurs for a variable associated with a AutoUUIDWidget must be 1");
        }

        if( getMinOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("minOccurs for a variable associated with a AutoUUIDWidget must be 1");
        }
        
        if( !getAttributeClass().equals(StringAttribute.class)){
            throw new InvalidEditorConfigurationException("A AutoUUIDWidget must be associated with a string attribute");
        }
        
    }

}
