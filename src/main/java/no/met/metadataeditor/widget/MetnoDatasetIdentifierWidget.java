package no.met.metadataeditor.widget;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import no.met.metadataeditor.Config;
import no.met.metadataeditor.EditorWidgetView;
import no.met.metadataeditor.InvalidEditorConfigurationException;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

/**
 * Widget used for the creation of the Met.no dataset identifiers.
 * 
 * At Met.no metadata can for a dataset can be generated using several
 * different method, only one of them being the metadata editor. Since
 * there are no central control for controlling the uniqueness of an
 * identifier each system cabable of generating a new identifier is
 * given a unique generator id which will be come part of the 
 * dataset id.
 * 
 * To ensure that not two users generates the same dataset identifier
 * the current time in base 36 is appended to the identifier.
 * 
 * Base 36 was choosen to make the number as short as possible while
 * at the same time not being case sensitive.
 */
public class MetnoDatasetIdentifierWidget extends EditorWidget {

    private static final long serialVersionUID = -3840255996843857527L;

    private String generatorId = "a";
    
    public MetnoDatasetIdentifierWidget(){
        super();
        Config config = new Config("/metadataeditor.properties", Config.ENV_NAME);
        generatorId = config.getRequired("widget.metnodatasetidentifier.generatorid");
    }
    
    /**
     * Constructor that takes the generator id as a parameter instead of reading it
     * from config. Only meant to be used from unit tests.
     * @param generatorId The generator id to use for the widget.
     */
    MetnoDatasetIdentifierWidget(String generatorId){
        super();
        this.generatorId = generatorId;
    }
    
    @Override
    public Map<String, String> getDefaultValue() {
        Map<String,String> defaultValue = new HashMap<String,String>();
        defaultValue.put("str", "");
        return defaultValue;
    }

    @Override
    protected void validateConfiguration(){
        
        if( getMaxOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("maxOccurs for a variable associated with a MetnoDatasetIdentifierWidget must be 1");
        }

        if( getMinOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("minOccurs for a variable associated with a MetnoDatasetIdentifierWidget must be 1");
        }
        
        if( !getAttributeClass().equals(StringAttribute.class)){
            throw new InvalidEditorConfigurationException("A MetnoDatasetIdentifierWidget must be associated with a string attribute");
        }        
        
    }
    
    @Override
    protected EditorVariableContent getContentForView(EditorWidgetView view){
        
        Map<String,String> values = view.getValues();
        String value = values.get("str");
        if( "".equals(value)){
            return super.getContentForView(view);
        }
        
        if( isNewValue(value) ){
            value = value + "-" + generatorId + "-" + base36Timestamp();
        }
        
        EditorVariableContent content = new EditorVariableContent();
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", value);
        content.setAttrs(sa);        
        
        return content;
        
    } 
    
    private boolean isNewValue(String value){

        if( Pattern.matches(".*-" + generatorId + "-[\\w\\d]+", value)){
            return false;
        }
        return true;
        
    }
    
    private String base36Timestamp(){
        int currentSeconds = (int) (System.currentTimeMillis() / 1000);
        return Integer.toString(currentSeconds, 36);
    }

}
