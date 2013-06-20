package no.met.metadataeditor.dataTypes;

import no.met.metadataeditor.EditorException;

public class InvalidTemplateException extends EditorException {

    private static final long serialVersionUID = -6610836549376243157L;

    public final static int INVALID_ATTRIBUTE_VALUE = 201;    
    public final static int INVALID_TAG = 202;
    public static final int MISSING_ATTRIBUTES = 203;
    
    public InvalidTemplateException(String msg, int errorCode) {
        super(msg, errorCode);
    }

}
