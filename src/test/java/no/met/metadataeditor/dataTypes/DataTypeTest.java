package no.met.metadataeditor.dataTypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataTypeTest {

    @Test
    public void testFloatValidation() {

        assertFalse(DataType.FLOAT.validate("").isValid);
        assertFalse(DataType.FLOAT.validate("test").isValid);
        assertFalse(DataType.FLOAT.validate("123 test").isValid);
        assertFalse(DataType.FLOAT.validate("7667test").isValid);
        assertFalse(DataType.FLOAT.validate("test123").isValid);
        assertFalse(DataType.FLOAT.validate("76 123123").isValid);
        assertFalse(DataType.FLOAT.validate("123123 test").isValid);
        assertFalse(DataType.FLOAT.validate("-12e123").isValid);

        assertTrue(DataType.FLOAT.validate("12").isValid);
        assertTrue(DataType.FLOAT.validate("12.0").isValid);
        assertTrue(DataType.FLOAT.validate("12.123132").isValid);
        assertTrue(DataType.FLOAT.validate("-12").isValid);
        assertTrue(DataType.FLOAT.validate("-12E123").isValid);
        assertTrue(DataType.FLOAT.validate("-0.2323").isValid);
        assertTrue(DataType.FLOAT.validate("0,4").isValid);
        assertTrue(DataType.FLOAT.validate("123133999999999912323232332233299992323").isValid);
        assertTrue(DataType.FLOAT.validate("123133999999999912323232332233299992323.87878").isValid);

    }

    @Test
    public void testIntegerValidation() {

        assertFalse(DataType.INTEGER.validate("").isValid);
        assertFalse(DataType.INTEGER.validate("test").isValid);
        assertFalse(DataType.INTEGER.validate("123 test").isValid);
        assertFalse(DataType.INTEGER.validate("4656rere").isValid);
        assertFalse(DataType.INTEGER.validate("fdff213").isValid);
        assertFalse(DataType.INTEGER.validate("3434 3434").isValid);
        assertFalse(DataType.INTEGER.validate("1.0").isValid);
        assertFalse(DataType.INTEGER.validate("0.1").isValid);
        assertFalse(DataType.INTEGER.validate("1e2").isValid);

        assertTrue(DataType.INTEGER.validate("12").isValid);
        assertTrue(DataType.INTEGER.validate("-12").isValid);
        assertTrue(DataType.INTEGER.validate("13213323432432432423432432492490239209029303923").isValid);
        assertTrue(DataType.INTEGER.validate("1E2").isValid);

    }

    @Test
    public void testDateValidation() {

        assertFalse(DataType.DATE.validate("").isValid);
        assertFalse(DataType.DATE.validate("2012").isValid);
        assertFalse(DataType.DATE.validate("20121212").isValid);
        assertFalse(DataType.DATE.validate("text").isValid);
        assertFalse(DataType.DATE.validate("2012-12-12test").isValid);
        assertFalse(DataType.DATE.validate("-2012-12-12").isValid);
        assertFalse(DataType.DATE.validate("2012-13-12").isValid);
        assertFalse(DataType.DATE.validate("2012-11-32").isValid);
        assertFalse(DataType.DATE.validate("2012-02-30").isValid);

        assertTrue(DataType.DATE.validate("2010-01-01").isValid);
        assertTrue(DataType.DATE.validate("1900-06-30").isValid);
        assertTrue(DataType.DATE.validate("2110-10-09").isValid);
    }

    @Test
    public void testDateTimeValidation() {

        assertFalse(DataType.DATETIME.validate("").isValid);
        assertFalse(DataType.DATETIME.validate("2012").isValid);
        assertFalse(DataType.DATETIME.validate("20121212").isValid);
        assertFalse(DataType.DATETIME.validate("text").isValid);
        assertFalse(DataType.DATETIME.validate("2012-12-12test").isValid);
        assertFalse(DataType.DATETIME.validate("-2012-12-12").isValid);
        assertFalse(DataType.DATETIME.validate("2012-13-12").isValid);
        assertFalse(DataType.DATETIME.validate("2012-11-32").isValid);
        assertFalse(DataType.DATETIME.validate("2012-02-30").isValid);
        assertFalse(DataType.DATETIME.validate("2010-01-01").isValid);
        assertFalse(DataType.DATETIME.validate("1900-06-30").isValid);
        assertFalse(DataType.DATETIME.validate("2110-10-09").isValid);
        assertFalse(DataType.DATETIME.validate("2110-10-09T00:00").isValid);
        assertFalse(DataType.DATETIME.validate("2110-10-09T00:12:43").isValid);
        assertFalse(DataType.DATETIME.validate("2110-10-09T32:12:43Z").isValid);


        assertTrue(DataType.DATETIME.validate("2010-01-01T00:00:00Z").isValid);
        assertTrue(DataType.DATETIME.validate("1900-02-01T12:59:30Z").isValid);
        assertTrue(DataType.DATETIME.validate("2110-12-31T20:00:00Z").isValid);
        assertTrue(DataType.DATETIME.validate("2110-10-09T12:12Z").isValid);
    }

    @Test
    public void testStringValidation(){

        assertFalse(DataType.STRING.validate("").isValid);

        assertTrue(DataType.STRING.validate("sdfasdf").isValid);
        assertTrue(DataType.STRING.validate("sdfasdf asdfsaf aoifu091213 13209183  13213213").isValid);

    }

    @Test
    public void testURLValidation(){

        assertFalse(DataType.URL.validate("").isValid);
        assertFalse(DataType.URL.validate("met.no").isValid);
        assertFalse(DataType.URL.validate("metadata").isValid);
        assertFalse(DataType.URL.validate("http://metadata.met.no\test").isValid);

        assertTrue(DataType.URL.validate("http://met.no").isValid);
        assertTrue(DataType.URL.validate("https://met.no/test").isValid);
        assertTrue(DataType.URL.validate("http://met.no?test=ewew&resrer=3121").isValid);
        assertTrue(DataType.URL.validate("ftp://met.no").isValid);
        assertTrue(DataType.URL.validate("opendap://met.no").isValid);

    }

    @Test
    public void testURIValidation(){

        assertFalse(DataType.URI.validate("").isValid);
        assertFalse(DataType.URI.validate("123:3434:test").isValid);

        assertTrue(DataType.URI.validate("http://met.no").isValid);
        assertTrue(DataType.URI.validate("https://met.no/test").isValid);
        assertTrue(DataType.URI.validate("http://met.no?test=ewew&resrer=3121").isValid);
        assertTrue(DataType.URI.validate("ftp://met.no").isValid);
        assertTrue(DataType.URI.validate("opendap://met.no").isValid);
        assertTrue(DataType.URI.validate("test").isValid);

        assertTrue(DataType.URI.validate("dfdf/dffd/").isValid);
        assertTrue(DataType.URI.validate("/test/er").isValid);

    }

    @Test
    public void testEmailValidation(){

        assertFalse(DataType.EMAIL.validate("").isValid);
        assertFalse(DataType.EMAIL.validate("test").isValid);
        assertFalse(DataType.EMAIL.validate("test@example.").isValid);

        assertTrue(DataType.EMAIL.validate("test@example.com").isValid);

    }

}
