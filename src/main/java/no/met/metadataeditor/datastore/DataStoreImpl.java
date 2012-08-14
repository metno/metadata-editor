package no.met.metadataeditor.datastore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import no.met.metadataeditor.EditorException;

abstract class DataStoreImpl implements DataStore {

    private final String SETUPFILE = "setup.xml";
    private final String CONFIGDIR = "config";
    private final String XMLDIR = "XML";

    private Document setupDoc = null;
    private Date setupDocDate = null;

    private Document getSetupDoc() {
        String path = makePath(CONFIGDIR, SETUPFILE);

        // use existing if not too old
        if (setupDoc != null && setupDocDate != null) {
            Date lastModified = getLastModified(path);
            if (!lastModified.after(setupDocDate)) {
                return setupDoc;
            }
        }

        // fetch new file
        setupDocDate = getLastModified(path);
        String setupDocStr = get(path);
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        try {
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            setupDoc = db.parse(new InputSource(new java.io.StringReader(setupDocStr)));
        } catch (SAXException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        } catch (ParserConfigurationException e) {
            Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return setupDoc;
    }

    /**
     * put a resource to the datastor
     * @param id
     * @param resource
     * @param username The username in the data store for the user doing the put action
     * @param password The password of the user doing the put action
     */
    abstract void put(String id, String resource, String username, String password);
    /**
     * fetch a resource as string from the datastore
     * @param id
     * @return
     */
    abstract String get(String id);
    /**
     * check the last change of a resource
     * @param id
     * @return date of last modification, or current date if not detectable
     */
    abstract java.util.Date getLastModified(String id);
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

    /**
     * @return A list of all available metadata in the data store
     */
    abstract List<String> list(String url);
    
    @Override
    public boolean writeMetadata(String recordIdentifier, String xml, String username, String password) {

        String url = metadataUrl(recordIdentifier);
        put(url, xml, username, password);
        return true;
    }

    @Override
    public boolean metadataExists(String recordIdentifier) {
        return exists(metadataUrl(recordIdentifier));
    }

    @Override
    public String readMetadata(String recordIdentifier) {

        return get(metadataUrl(recordIdentifier));
    }

    @Override
    public String readTemplate(String recordIdentifier) {

        String metadata = readMetadata(recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(getSupportedFormats(), metadata);
        String templateUrl = templateUrl(format);

        if (!exists(templateUrl)) {
            throw new EditorException("Template does not exist: " + templateUrl);
        }

        return get(templateUrl);
    }

    @Override
    public String readTemplate(SupportedFormat format){
        String templateUrl = templateUrl(format);

        List<SupportedFormat> formats = getSupportedFormats();

        if( !formats.contains(format) ){
            throw new IllegalArgumentException("Format not supported by project: " + format);
        }

        if (!exists(templateUrl)) {
            throw new EditorException("Template does not exist: " + templateUrl);
        }

        return get(templateUrl);
    }

    @Override
    public String readEditorConfiguration(String recordIdentifier) {

        String metadata = readMetadata(recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(getSupportedFormats(), metadata);
        String configurationUrl = editorConfigUrl(format);

        if (!exists(configurationUrl)) {
            throw new EditorException("Template does not exist: " + configurationUrl);
        }

        return get(configurationUrl);

    }

    @Override
    public String readResource(String resourceIdentifier) {

        String resourceUrl = resourceUrl(resourceIdentifier);

        if (!exists(resourceUrl)) {
            throw new EditorException("Resource does not exist: " + resourceUrl);
        }
        return get(resourceUrl);
    }

    private String templateUrl(SupportedFormat format) {
        return makePath(CONFIGDIR, format.templateName());
    }

    private String editorConfigUrl(SupportedFormat format){

        return makePath(CONFIGDIR, format.editorConfigName());
    }

    private String resourceUrl(String resourceIdentifier){

        return makePath(resourceIdentifier);
    }

    private String metadataUrl(String recordIdentifier) {

        return makePath(XMLDIR, recordIdentifier + ".xml");

    }
    
    private String metadataDirUrl() {
        return makePath(XMLDIR);
    }

    @Override
    public List<SupportedFormat> getSupportedFormats() {
        Document doc = getSetupDoc();
        return DataStoreUtils.parseSupportedFormats(doc);
    }

    @Override
    public List<String> availableMetadata(){
        
        List<String> filenames = list(metadataDirUrl());
        List<String> identifiers = new ArrayList<String>();
        for( String filename : filenames ){            
            identifiers.add(FilenameUtils.removeExtension(filename));            
        }
        
        // we sort the identifiers for simple automatic testing.        
        Collections.sort(identifiers);
        return identifiers;
        
    }
}
