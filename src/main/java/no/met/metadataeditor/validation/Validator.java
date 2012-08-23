package no.met.metadataeditor.validation;

import java.io.IOException;

public interface Validator {

    /**
     * This is a simplified validation class. It is largely compatible with
     * javax.xml.validation.Validator, except that exceptions should be rethrown.
     * In addition, this Validator should be thread-safe and reentrant.
     *
     * @param xml the xml-string to validate
     * @throws ValidatorException thrown when problems during validation occur
     * @see
     */
    public void validate(javax.xml.transform.Source source) throws ValidatorException, IOException;

}
