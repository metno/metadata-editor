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


@Path("/")
public class RestController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{identifier}")
    public Map<String, EditorVariable> retrieve(@PathParam("identifier") String identifier) {

        HashMap<String,EditorVariable> varMap = new HashMap<String,EditorVariable>();

        EditorVariable var1 = new EditorVariable();
        EditorVariableContent cont1 = new EditorVariableContent();
        var1.addContent(cont1);
        cont1.setAttrs(new SingleValueData("Hans Klaus"));
        varMap.put("contact", var1);

        EditorVariableContent cont2 = new EditorVariableContent();
        EditorVariable var2 = new EditorVariable();
        cont2.setAttrs(new SingleValueData("Metadata editor"));
        var2.addContent(cont2);
        varMap.put("PIName", var2);

        EditorVariable var3 = new EditorVariable();
        EditorVariableContent cont3_1 = new EditorVariableContent();
        cont3_1.setAttrs(new SingleValueData("precipitation"));
        var3.addContent(cont3_1);
        EditorVariableContent cont3_2 = new EditorVariableContent();
        cont3_2.setAttrs(new SingleValueData("millimeter"));
        var3.addContent(cont3_2);
        varMap.put("variableList", var3);


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