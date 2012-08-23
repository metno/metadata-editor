package no.met.metadataeditor.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;
import no.met.metadataeditor.validation.ValidatorException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.jersey.api.Responses;

@Path("/validator/")
public class SimplePutValidator {

    @Context
    HttpServletRequest request;

    /**
     * Get the XML for a metadata record.
     * @param xml the document to validate
     * @return The an http-ok response. Returns a HTTP 400 if the record does not validate
     */
    @POST
    @Path("mm2")
    @ServiceDescription("Validate an mm2 string")
    public Response validateMM2(@FormParam("xml") String xml) throws ValidatorException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema mm2schema = sf.newSchema(getClass().getResource("/schemas/MM2.xsd"));
            assert(mm2schema != null);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setSchema(mm2schema);
            SAXParser sp = spf.newSAXParser();
            sp.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())), new DefaultHandler() {
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
        } catch (ParserConfigurationException e) {
            Logger.getLogger(SimplePutValidator.class.getName()).log(Level.SEVERE, null, e);
            return Response.serverError().build();
        } catch (IOException e) {
            Logger.getLogger(SimplePutValidator.class.getName()).log(Level.SEVERE, null, e);
            return Response.serverError().build();
        } catch (SAXException se) {
            throw new ValidatorException(se);
        }

        return Response.ok("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><validation status=\"success\" />", MediaType.TEXT_XML).build();
    }

    /**
     * Get the XML for a metadata record.
     * @param project the 'webdav' project, i.e. where the validator configuration lies in config/setup.xml
     * @param tag reference within the 'internalValidators/validator@tag' in setup.xml
     * @param xml the document to validate
     * @return The an http-ok response. Returns a HTTP 400 if the record does not validate
     */
    @POST
    @Path("{project}/{tag}")
    @ServiceDescription("Validate a xml-string for a project and a format indicated with a tag")
    public Response validate(@PathParam("project") String project, @PathParam("tag") String tag, @FormParam("xml") String xml) throws ValidatorException {
        DataStore datastore = DataStoreFactory.getInstance(project);
        try {
            datastore.getValidator(tag).validate(new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes()))));
            return Response.ok("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><validation status=\"success\" />", MediaType.TEXT_XML).build();
        } catch (IllegalArgumentException e1) {
            Logger.getLogger(SimplePutValidator.class.getName()).log(Level.SEVERE, null, e1);
            return Responses.notFound().build();
        } catch (IOException e) {
            Logger.getLogger(SimplePutValidator.class.getName()).log(Level.SEVERE, null, e);
            return Response.serverError().build();
        }

    }


}
