package no.met.metadataeditor.dataTypes;

import org.xml.sax.SAXException;

public class UndefinedEditorVariableException extends SAXException {

    private static final long serialVersionUID = -661617075332607371L;

    public UndefinedEditorVariableException() {
    }

    public UndefinedEditorVariableException(String arg0) {
        super(arg0);
    }

}
