package no.met.metadataeditor.validation;

import org.xml.sax.SAXException;

public class ValidatorException extends Exception {

    public ValidatorException(SAXException ex) {
        super(ex);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
