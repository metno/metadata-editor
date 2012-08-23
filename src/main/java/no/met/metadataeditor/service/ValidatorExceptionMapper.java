package no.met.metadataeditor.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import no.met.metadataeditor.validation.ValidatorException;

@Provider
public class ValidatorExceptionMapper implements ExceptionMapper<ValidatorException> {

    @Override
    public Response toResponse(ValidatorException ex) {
        return Response.status(400)
                .entity(String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><validation status=\"fail\">%s</validation>", ex.getMessage()))
                .type(MediaType.TEXT_XML).build();
    }


}
