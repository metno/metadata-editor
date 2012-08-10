package no.met.metadataeditor.datastore;

import java.io.InputStream;
import java.util.List;

import no.met.metadataeditor.EditorException;

abstract class DataStoreImpl implements DataStore {

    private final String SUPPORTED_FORMATS = "supportedFormats.txt";
    private final String CONFIGDIR = "config";
    private final String XMLDIR = "XML";

    /**
     * put a resource to the datastor
     * @param id
     * @param resource
     */
    abstract void put(String id, String resource);
    /**
     * fetch a resource as string from the datastore
     * @param id
     * @return
     */
    abstract String get(String id);
    /**
     * check if a resource exists
     * @param id
     * @return
     */
    abstract boolean exists(String id);
    /**
     * build the full path for a datastore
     * @param paths
     * @return
     */
    abstract String makePath(String... paths);

    @Override
    public boolean writeMetadata(String project, String recordIdentifier, String xml) {

        String url = metadataUrl(project, recordIdentifier);
        put(url, xml);
        return true;
    }

    @Override
    public boolean projectExists(String project) {
        return exists(makePath(project));
    }

    @Override
    public boolean metadataExists(String project, String recordIdentifier) {
        return exists(metadataUrl(project, recordIdentifier));
    }

    @Override
    public String readMetadata(String project, String recordIdentifier) {

        return get(metadataUrl(project, recordIdentifier));
    }

    @Override
    public String readTemplate(String project, String recordIdentifier) {

        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(getSupportedFormats(project), metadata);
        String templateUrl = templateUrl(project, format);

        if (!exists(templateUrl)) {
            throw new EditorException("Template does not exist: " + templateUrl);
        }

        return get(templateUrl);
    }
    
    @Override
    public String readTemplate(String project, SupportedFormat format){
        String templateUrl = templateUrl(project, format);

        List<SupportedFormat> formats = getSupportedFormats(project);
        
        if( !formats.contains(format) ){
            throw new IllegalArgumentException("Format not supported by project: " + format);
        }
        
        if (!exists(templateUrl)) {
            throw new EditorException("Template does not exist: " + templateUrl);
        }

        return get(templateUrl);        
    }

    @Override
    public String readEditorConfiguration(String project, String recordIdentifier) {

        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(getSupportedFormats(project), metadata);
        String configurationUrl = editorConfigUrl(project, format);

        if (!exists(configurationUrl)) {
            throw new EditorException("Template does not exist: " + configurationUrl);
        }

        return get(configurationUrl);

    }

    @Override
    public String readResource(String project, String resourceIdentifier) {

        String resourceUrl = resourceUrl(project, resourceIdentifier);

        if (!exists(resourceUrl)) {
            throw new EditorException("Resource does not exist: " + resourceUrl);
        }
        return get(resourceUrl);
    }

    private String templateUrl(String project, SupportedFormat format) {
        return makePath(project, CONFIGDIR, format.templateName());
    }

    private String editorConfigUrl(String project, SupportedFormat format){

        return makePath(project, CONFIGDIR, format.editorConfigName());
    }

    private String resourceUrl(String project, String resourceIdentifier){

        return makePath(project, resourceIdentifier);
    }

    private String metadataUrl(String project, String recordIdentifier) {

        return makePath(project, XMLDIR, recordIdentifier + ".xml");

    }

    @Override
    public List<SupportedFormat> getSupportedFormats(String project) {
        return DataStoreUtils.parseSupportedFormats(get(makePath(project, "config", SUPPORTED_FORMATS)));
    }

}
