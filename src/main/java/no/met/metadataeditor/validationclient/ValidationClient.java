package no.met.metadataeditor.validationclient;

/**
 * Interface for clients to validation services.
 */
public interface ValidationClient {

    /**
     * Check if the XML is valid against some type of schema. The schema is
     * determined by the validation service the validation client is contacting.
     *
     * @param xml The XML to validation.
     * @return A ValidationResponse with information about the validation result.
     */
    public ValidationResponse validate(String xml);

}
