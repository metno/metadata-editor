package no.met.metadataeditor.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

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
    public Response validateMM2(@FormParam("xml") String xml) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        sf.setErrorHandler(new MyErrorHandler());
        try {
            Schema mm2schema = sf.newSchema(getClass().getResource("/schemas/MM2.xsd"));
            assert(mm2schema != null);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setSchema(mm2schema);
            SAXParser sp = spf.newSAXParser();
            System.err.println(xml);
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
        }

        return Response.ok("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><validation status=\"success\" />", MediaType.TEXT_XML).build();
    }

}
