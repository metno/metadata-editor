package no.met.metadataeditor.service;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import no.met.metadataeditor.datastore.DataStore;
import no.met.metadataeditor.datastore.DataStoreFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.jersey.api.Responses;

/**
 * REST API used to communicate with the metadata editor from other systems.
 */
@ApplicationPath("service")
@Path("/metaedit_api/")
public class RESTApi extends Application {

    @Context
    HttpServletRequest request;

    @GET
    @Path("")
    @Produces("application/xml")
    @ServiceDescription("Return the list of services with parameters.")
    public Response capabilities() throws ParserConfigurationException{
        Document d = ServiceDescriptionGenerator.getXMLServiceDescription(this.getClass());
        return Response.ok(d).build();
    }

    /**
     * Get the XML for a metadata record.
     * @param project The project to read from.
     * @param record The record to read.
     * @return The XML for a metadata record. Returns a HTTP 404 if the record does not exist.
     */
    @GET
    @Path("{project}/{record}")
    @ServiceDescription("Get the XML for the metadata record.")
    public Response getMetadata(@PathParam("project") String project, @PathParam("record") String record ){

        DataStore datastore = DataStoreFactory.getInstance(project);
        if(!datastore.metadataExists(record)){
            return Responses.notFound().build();
        }

        String metadata = datastore.readMetadata(record);

        return Response.ok(metadata, MediaType.TEXT_XML).build();
    }

    /**
     * Get the URL to edit a metadata record, and as an option suggest a new
     * version of the XML record. If new metadata is included in the request and
     * is different from the current version, the metadata is stored in a
     * temporary file. Human interaction is then required to make it the
     * new version.
     *
     * Posting metadata to a record that does not exist causes the record to be
     * created.
     *
     * @param project
     *            The project the metadata record is in.
     * @param record
     *            The identifier for the record.
     * @param metadata
     *            (optional) The suggested new metadata for the record.
     * @return Either an url to the metadata editor or a metadata comparison editor for selecting the correct version.
     */
    @POST
    @Path("{project}/{record}")
    @ServiceDescription("Get the URL to edit a metadata record and as an option suggest a new version of the XML in the metadata parameter")
    public Response postMetadata(@PathParam("project") String project, @PathParam("record") String record, String metadata){

        DataStore datastore = DataStoreFactory.getInstance(project);
        boolean metadataExists = datastore.metadataExists(record);

        Response response;
        if(!metadataExists && metadata == null ){
            response = Responses.notFound().build();
        } else if ( !metadataExists && metadata != null ){

            datastore.writeMetadata(record, metadata, datastore.getDefaultUser(), datastore.getDefaultPassword());

            response = Response.ok(getEditorUrl(project, record)).build();
        } else if( metadataExists && metadata == null ){
            response = Response.ok(getEditorUrl(project, record)).build();
        } else {

            if( metadataEqual(metadata, datastore.readMetadata(record))) {
                response = Response.ok(getEditorUrl(project, record)).build();
            } else {

                datastore.writeMetadata(record + DataStore.THEIRS_IDENTIFIER, metadata, datastore.getDefaultUser(), datastore.getDefaultPassword());
                response = Response.ok(getCompareUrl(project, record)).build();
            }

        }


        return response;
    }

    private String getEditorUrl(String project, String record){

        return getBaseUrl(project, record) + "editor.xhtml?project=" + project + "&record=" + record;
    }

    private String getCompareUrl(String project, String record) {
        return getBaseUrl(project, record) + "compare.xhtml?project=" + project + "&record=" + record;
    }

    private String getBaseUrl(String project, String record){

        String url = request.getScheme() + "://" + request.getServerName();

        if(request.getServerPort() != 80){
            url += ":" + request.getServerPort();
        }

        String startPath = request.getRequestURI();

        int removeLength = 0;
        removeLength += record.length() + 1; // the length of '/<record>
        removeLength += project.length() + 1; // the length of '/<project>
        removeLength += request.getServletPath().length(); // the length of the application path
        removeLength += "metaedit_api/".length(); // length of the service path

        startPath = startPath.substring(0, startPath.length() - removeLength);

        return url + startPath + "/";

    }

    private boolean metadataEqual(String metadata1, String metadata2){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document doc1 = db.parse(new InputSource(new StringReader(metadata1)));
            doc1.normalizeDocument();

            Document doc2 = db.parse(new InputSource(new StringReader(metadata2)));
            doc2.normalizeDocument();

            return doc1.isEqualNode(doc2);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

}
