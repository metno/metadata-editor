package no.met.metadataeditor;

public class InvalidEditorConfigurationException extends EditorException {

    private static final long serialVersionUID = 4902574371399601804L;

    public static final int UNKNOWN_VARIABLE = 301; // the variable name used in config is not in template
    public static final int INVALID_WIDGET_CONFIG = 302; // the configuration for a widget is not valid
    
    public InvalidEditorConfigurationException(String msg, int errorCode){
        super(msg, errorCode);
    }
    
}
