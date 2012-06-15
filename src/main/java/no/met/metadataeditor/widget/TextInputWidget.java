package no.met.metadataeditor.widget;

/**
 * Class that is used to hold the values for a TextInputWidget 
 */
public class TextInputWidget extends EditorWidget {

    public TextInputWidget(String widgetId) {
        super(widgetId);
    }

    
    public String getValue() {
        return super.getValue("value");
    }
    
    public void setValue(String value){
        super.setValue("value", value);
    }
    
    @Override
    public String getType() {
        return "text-input";
    }

}
