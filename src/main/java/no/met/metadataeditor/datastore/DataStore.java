package no.met.metadataeditor.datastore;

import java.util.List;

import no.met.metadataeditor.validation.Validator;
import no.met.metadataeditor.validationclient.ValidationClient;

/**
 * Interface supported by all data stores.
 *
 * A data store is where the editor will find metadata, configurations and resources for a single project
 */
public interface DataStore {

    /**
     * Constant used to identify a suggested version of a record. A user most later
     * choose between the current version and the THEIRS version.
     */
    String THEIRS_IDENTIFIER = ".theirs";

    /**
     * Write the metadata to the datastore.
     * @param recordIdentifier The recordIdentifer for the metadata record to write to.
     * @param xml The XML that should be written. The XML will be written directly without further processing.
     * @return Returns true on success and throws a EditorException on error.
     */
    boolean writeMetadata(String recordIdentifier, String xml, String username, String password);


    /**
     * @param recordIdentifier The metadata record to check if exists.
     * @return Returns true if the metadata record exists in the datastore. Returns false otherwise.
     */
    boolean metadataExists(String recordIdentifier);

    /**
     *
     * @return list of all supported formats in this data store as in file 'supportedFormats.txt'
     */
    List<SupportedFormat> getSupportedFormats();

    /**
     * @param recordIdentifier The record identifier for the metadata to read.
     * @return The raw XML for the metadata on success. Throws and EditorException on error.
     */
    String readMetadata(String recordIdentifier);

    /**
     * @param recordIdentifier The record identifier for the metadata we want a template for.
     * @return The raw XML for the template on success. Throws and EditorException on error.
     */
    String readTemplate(String recordIdentifier);

    /**
     * This function is used when you need to read a template but do not yet have a record.
     * @param format The format to read the template for
     * @return The raw XML for the template for the supported format.
     */
    String readTemplate(SupportedFormat format);

    /**
     * @param recordIdentifier The record identifier for the metadata we want a configuration for.
     * @return The raw XML for the metadata on success. Throws and EditorException on error.
     */
    String readEditorConfiguration(String recordIdentifier);

    /**
     * @param resourceIdentifier The identifier of the resource to read.
     * @return The raw string content of the resource on success. Throws and EditorException on error.
     */
    String readResource(String resourceIdentifier);

    /**
     * @param username The username of the user
     * @param password The un-hashed password of the user.
     * @return True if the user has write access to the project. False otherwise
     */
    boolean userHasWriteAccess(String username, String password);

    /**
     * @return A list of record identifiers for the available metadata in the datastore
     */
    List<String> availableMetadata();

    /**
     * @return Get the default username for the datastore.
     */
    String getDefaultUser();

    /**
     * @return Get the default password for the datastore.
     * @return
     */
    String getDefaultPassword();

    /**
     * Delete a record from the editor metadata repository.
     * @param recordIdentifier The identifier of the record to delete.
     * @param username The username of the user
     * @param password The un-hashed password of the user.
     * @return True if the record was delete. False otherwise, i.e. the record does not exist
     */
    boolean deleteMetadata(String recordIdentifier, String username, String password);


    /**
     * Get a validator from the setup-file with the provided tag. Tag should, but does
     * not need to be one of the supported formats.
     *
     * @param tag
     * @return a validator to validate a string.
     * @throws IllegalArgumentException if tag does not exists in setup
     */
    Validator getValidator(String tag) throws IllegalArgumentException;

    /**
     * Get a ValidationClient for the record if it has been configured for the format.
     * @param recordMetadata The metadata for the record to get the validation client for
     * @return The ValiationClient for the record or null if it has not been configured.
     */
    ValidationClient getValidationClient(String recordMetadata);

}
