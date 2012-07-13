package no.met.metadataeditor;

/**
 * Exception used by the ConfigUtils to report runtime errors.
 */
public class ConfigException extends RuntimeException {

    private static final long serialVersionUID = 6187133211375800371L;

    public ConfigException(String message){
        super(message);
    }

    public ConfigException(String message, Throwable cause){
        super(message,cause);
    }

    public ConfigException(Throwable cause){
        super(cause);
    }

}
