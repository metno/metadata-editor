package no.met.metadataeditor.validation;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SchemaValidator implements Validator {
    private Schema schema;
    public SchemaValidator(Schema schema) {
        this.schema = schema;
    }

    @Override
    public void validate(Source source) throws ValidatorException, IOException {
        javax.xml.validation.Validator schemaValidator = schema.newValidator();
        schemaValidator.setErrorHandler(new DefaultHandler() {
            @Override
            public void error(SAXParseException ex) throws SAXException {
                throw ex;
            }
            @Override
            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }
            @Override
            public void warning(SAXParseException ex) throws SAXException {
                return;
            }
        });
        try {
            schemaValidator.validate(source);
        } catch (SAXException ex) {
            throw new ValidatorException(ex);
        }
    }

}
