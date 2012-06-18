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
    public Map<String, List<EditorVariable>> retrieve(@PathParam("identifier") String identifier) {
       
        HashMap<String,List<EditorVariable>> varMap = new HashMap<String,List<EditorVariable>>();

        ArrayList<EditorVariable> vars1 = new ArrayList<EditorVariable>();        
        EditorVariable var1 = new EditorVariable();
        var1.setAttrs(new SingleValueData("Hans Klaus"));        
        vars1.add(var1);
        varMap.put("contact", vars1);
        
        ArrayList<EditorVariable> vars2 = new ArrayList<EditorVariable>();        
        EditorVariable var2 = new EditorVariable();
        var2.setAttrs(new SingleValueData("Metadata editor"));        
        vars2.add(var2);              
        varMap.put("PIName", vars2);

        ArrayList<EditorVariable> vars3 = new ArrayList<EditorVariable>();        
        EditorVariable var3_1 = new EditorVariable();
        var3_1.setAttrs(new SingleValueData("precipitation"));        
        vars3.add(var3_1);
        EditorVariable var3_2 = new EditorVariable();
        var3_2.setAttrs(new SingleValueData("millimeter"));        
        vars3.add(var3_2);                      
        varMap.put("variableList", vars3);
        
        
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