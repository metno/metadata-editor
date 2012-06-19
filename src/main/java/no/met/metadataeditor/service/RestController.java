package no.met.metadataeditor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.LatLonBBAttributes;
import no.met.metadataeditor.dataTypes.StringAttributes;


@Path("/")
public class RestController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{identifier}")
    public Map<String, EditorVariable> retrieve(@PathParam("identifier") String identifier) {

        HashMap<String,EditorVariable> varMap = new HashMap<String,EditorVariable>();

        if ("12345".equals(identifier)) {
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

        return varMap;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{identifier}")
    public Map<String, List<EditorVariable>> store(@PathParam("identifier") String identifier) {

        HashMap<String,List<EditorVariable>> varMap = new HashMap<String,List<EditorVariable>>();
        ArrayList<EditorVariable> vars = new ArrayList<EditorVariable>();
        varMap.put("dummy", vars);


        return varMap;
    }


}