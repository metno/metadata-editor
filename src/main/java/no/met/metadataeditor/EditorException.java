package no.met.metadataeditor;

public class EditorException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7731932552297906249L;

    public EditorException(String message){
        super(message);
    }

    public EditorException(String message, Throwable cause) {
        super(message,cause);
    }

    public EditorException(Throwable e) {
        super(e);
    }
}
