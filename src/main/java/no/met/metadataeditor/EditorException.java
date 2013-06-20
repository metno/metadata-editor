package no.met.metadataeditor;

public class EditorException extends RuntimeException {
    
    // available error codes
    public final static int GENERAL_ERROR_CODE = 100; // exception that cannot be futher classified    
    public final static int TEMPLATE_PARSE_ERROR = 101; // an error when parsing the template
    public final static int IO_ERROR = 102; // error caused by and IOException
    public final static int METADATA_PARSE_ERROR = 103; // error caused when parsing the metadata content
    public final static int EDITOR_CONFIG_UNMARSHAL_ERROR = 104; // error when converting editor config to objects
    public final static int MISSING_PROJECT_CONFIG = 105; // project that user ask for has not been configured
    public final static int MISSING_TEMPLATE = 106; // the template for a format is missing
    public final static int MISSING_EDITOR_CONFIG = 107; //the editor configuration for a format is missing
    public final static int UNSUPPORTED_FORMAT = 108; // the format for the content is not supported.
    public final static int MISSING_METADATA_RESOURCE = 109; // the metadata resources is missing.
    public final static int SETUP_XML_ERROR = 110; // error in the setup.xml file
    
    /**
     * The unique error code within the exception type for this type of exception. 
     */
    public final int errorCode;
    
    /**
     *
     */
    private static final long serialVersionUID = -7731932552297906249L;

    public EditorException(String message, int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public EditorException(String message, Throwable cause, int errorCode) {
        super(message,cause);
        this.errorCode = errorCode;        
    }

    public EditorException(Throwable e, int errorCode) {
        super(e);
        this.errorCode = errorCode;        
    }
    
    // needed for use of error code in the JSF.
    public int getErrorCode(){
        return errorCode;
    }
}
