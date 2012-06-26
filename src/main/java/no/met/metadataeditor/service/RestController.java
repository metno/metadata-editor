package no.met.metadataeditor.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.met.metadataeditor.dataTypes.EditorTemplate;

import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.LatLonBBAttributes;
import no.met.metadataeditor.dataTypes.StringAttributes;


@Path("/")
public class RestController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{identifier}")
    public Response retrieve(@PathParam("identifier") String identifier) {

        Map<String,EditorVariable> varMap = new HashMap<String,EditorVariable>();

        if ("4443".equals(identifier)) {
            URL url = getClass().getResource("/defaultConfig/mm2Template.xml");
            try {
                EditorTemplate et = new EditorTemplate(new InputSource(url.openStream()));
                //et.addData(new InputSource(xmlUrl.openStream()));
                varMap = et.getTemplate();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if ("4444".equals(identifier)) {
            URL url = getClass().getResource("/defaultConfig/mm2Template.xml");
            URL xmlUrl = getClass().getResource("/defaultConfig/exampleMM2.xml");
            try {
                EditorTemplate et = new EditorTemplate(new InputSource(url.openStream()));
                et.addData(new InputSource(xmlUrl.openStream()));
                varMap = et.getTemplate();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } else if ("12345".equals(identifier)) {
            EditorVariable var1 = new EditorVariable(new StringAttributes());
            EditorVariableContent cont1 = new EditorVariableContent();
            cont1.setAttrs(new StringAttributes("Hans Klaus"));
            var1.addContent(cont1);
            varMap.put("contact", var1);

            EditorVariableContent cont2 = new EditorVariableContent();
            EditorVariable var2 = new EditorVariable(new StringAttributes());
            cont2.setAttrs(new StringAttributes("Metadata editor"));
            var2.addContent(cont2);
            varMap.put("PIName", var2);

            EditorVariable var3 = new EditorVariable(new StringAttributes());
            EditorVariableContent cont3_1 = new EditorVariableContent();
            cont3_1.setAttrs(new StringAttributes("precipitation"));
            var3.addContent(cont3_1);
            EditorVariableContent cont3_2 = new EditorVariableContent();
            cont3_2.setAttrs(new StringAttributes("millimeter"));
            var3.addContent(cont3_2);
            varMap.put("variableList", var3);

            EditorVariableContent cont4 = new EditorVariableContent();
            LatLonBBAttributes llbb = new LatLonBBAttributes();
            EditorVariable var4 = new EditorVariable(llbb);
            llbb.setEast(180);
            llbb.setWest(-180);
            llbb.setNorth(90);
            llbb.setSouth(60);
            cont4.setAttrs(llbb);
            var4.addContent(cont4);
            varMap.put("llbb", var4);

        }


        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writerWithType(new TypeReference<Map<String,EditorVariable>>() { }).writeValueAsString(varMap);
            return Response.ok(jsonContent, MediaType.APPLICATION_JSON).build();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }

    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{identifier}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response store(@PathParam("identifier") String identifier, String jsonContent) {


        HashMap<String, EditorVariable> varMap;
        try {
            ObjectMapper mapper = new ObjectMapper();
            varMap = mapper.readValue(jsonContent, new TypeReference<HashMap<String,EditorVariable>>() {});
        } catch (JsonParseException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }


        return Response.ok().build();
    }


}