package no.met.metadataeditor.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Path("/")
public class RestController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("retrieve")
    public String retrieve(@QueryParam("identifier") String identifier) {

        return "ole er doffen";
        
    }

}