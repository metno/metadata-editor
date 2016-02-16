package no.met.metadataeditor.dataTypes;

import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.BigIntegerValidator;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

public enum DataType {

    INTEGER {
        @Override
        public DataAttributeValidationResult validate(String value) {

            BigIntegerValidator v = BigIntegerValidator.getInstance();

            if( v.validate(value) == null ){
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid integer.");
            } else {
                return new DataAttributeValidationResult(true, null);
            }
        }
    },
    FLOAT {
        @Override
        public DataAttributeValidationResult validate(String value) {

            BigDecimalValidator v = BigDecimalValidator.getInstance();

            if( v.validate(value, Locale.US) == null ){
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid number.");
            } else {
                return new DataAttributeValidationResult(true, null);
            }
        }
    },
    DATE {
        @Override
        public DataAttributeValidationResult validate(String value) {

            DateValidator v = DateValidator.getInstance();
            String dateformat = "yyyy-MM-dd";

            if( v.validate(value, dateformat) == null ){
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid date .");
            } else {
                return new DataAttributeValidationResult(true, null);
            }

        }
    },
    DATETIME {
        @Override
        public DataAttributeValidationResult validate(String value) {

            DateValidator v = DateValidator.getInstance();
            String dateformatUTCNoSec = "yyyy-MM-dd'T'HH:mm'Z'";
            String dateformatUTCSec = "yyyy-MM-dd'T'HH:mm:ss'Z'";

            if( v.validate(value, dateformatUTCNoSec) != null ){
                return new DataAttributeValidationResult(true, null);
            } if( v.validate(value, dateformatUTCSec) != null ){
                return new DataAttributeValidationResult(true, null);
            } else {
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid date time.");
            }
        }
    },
    STRING {
        @Override
        public DataAttributeValidationResult validate(String value) {

            if( "".equals(value)){
                return new DataAttributeValidationResult(false, "You should input a value");
            }

            return new DataAttributeValidationResult(true, null);
        }
    },
    URI {
        @Override
        public DataAttributeValidationResult validate(String value) {

            if( "".equals(value)){
                return new DataAttributeValidationResult(false, "You must input a value");
            }

            try {
                new java.net.URI(value);
                return new DataAttributeValidationResult(true, null);
            } catch (URISyntaxException e) {
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid URI.");
            }
        }
    },
    URL {
        @Override
        public DataAttributeValidationResult validate(String value) {

            UrlValidator v = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);

            if( !v.isValid(value)){
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid URL");
            } else {
                return new DataAttributeValidationResult(true, null);
            }

        }
    },
    EMAIL {
        @Override
        public DataAttributeValidationResult validate(String value) {

            EmailValidator v = EmailValidator.getInstance();

            if( !v.isValid(value)){
                return new DataAttributeValidationResult(false, "'" + value + "' is not a valid email.");
            } else {
                return new DataAttributeValidationResult(true, null);
            }
        }
    };




    public abstract DataAttributeValidationResult validate(String value);
}
