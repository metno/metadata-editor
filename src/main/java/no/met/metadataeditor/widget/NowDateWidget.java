package no.met.metadataeditor.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import no.met.metadataeditor.EditorWidgetView;
import no.met.metadataeditor.InvalidEditorConfigurationException;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.dataTypes.attributes.DateAttribute;

/**
 * Widget that auto generates a new date stamp before the data is saved.
 */
public class NowDateWidget extends EditorWidget {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");    
    
    @Override
    public Map<String, String> getDefaultValue() {
        
        Map<String,String> values = new HashMap<>();
        values.put("date", nowTimeStamp());
        return values;
    }

    @Override
    protected void validateConfiguration(){
        
        if( getMaxOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("maxOccurs for a variable associated with a NowDateWidget must be 1", InvalidEditorConfigurationException.INVALID_WIDGET_CONFIG);
        }

        if( getMinOccurs() != 1 ){
            throw new InvalidEditorConfigurationException("minOccurs for a variable associated with a NowDateWidget must be 1", InvalidEditorConfigurationException.INVALID_WIDGET_CONFIG);
        }
        
        if( !getAttributeClass().equals(DateAttribute.class)){
            throw new InvalidEditorConfigurationException("A NowDateWidget must be associated with a string attribute", InvalidEditorConfigurationException.INVALID_WIDGET_CONFIG);
        }        
        
    }
    
    @Override
    protected EditorVariableContent getContentForView(EditorWidgetView view){
        
        // this widget ignores the previous content and creates a new value instead.
        EditorVariableContent content = new EditorVariableContent();
        DataAttribute da = new DateAttribute();
        da.addAttribute("date", nowTimeStamp());
        content.setAttrs(da);
        return content;
        
    }    
    
    /**
     * @return Get a timestamp on the form YYYY-MM-DD for the current time.
     */
    public static String nowTimeStamp() {
        
        Date now = new Date();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String strDate = dateFormat.format(now);
        return strDate;

    }

}
