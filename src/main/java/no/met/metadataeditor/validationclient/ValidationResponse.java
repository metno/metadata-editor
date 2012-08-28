package no.met.metadataeditor.validationclient;

/**
 * A simple value object used to aggregate several validation response values
 * into one object.
 *
 * It has no operations and all fields are final.
 */
public class ValidationResponse {

    public final boolean success;

    public final String message;

    public ValidationResponse(boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
