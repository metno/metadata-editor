package no.met.metadataeditor.datastore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import no.met.metadataeditor.EditorException;
import no.met.metadataeditor.validation.SchemaValidator;
import no.met.metadataeditor.validation.Validator;
import no.met.metadataeditor.validationclient.ValidationClient;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

abstract class DataStoreImpl implements DataStore {

    private static final String SETUPFILE = "setup.xml";
    private static final String CONFIGDIR = "config";
    private static final String XMLDIR = "XML";

    private Document setupDoc = null;
    private Date setupDocDate = null;
    private final Map<String, Validator> validator = new HashMap<String, Validator>();
    private final Map<String, Date> lastDataStoreDate = new HashMap<String, Date>();

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
     * build the full path for a datastore, for using
     * within the DataStore (i.e. for File, this is without prefix)
     * @param paths
     * @return
     */
    String makePath(String... paths) {
        return makeURL(paths).toString();
    }

    /**
     * build the full path for a datastore, including protocol-prefix
     * @param paths
     * @return
     */
    abstract URL makeURL(String... paths);



    /**
     * @return A list of all available metadata in the data store
     */
    abstract List<String> list(String url);

    /**
     * Delete a metadata record from the data store
     * @param url The record to delete.
     * @param username The username in the data store for the user doing the delete.
     * @param password The password of the user doing the delete.
     * @return True if the record was delete. False otherwise.
     */
    abstract boolean delete(String url, String username, String password);

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

    @Override
    public boolean deleteMetadata(String recordIdentifier, String username, String password){

        return delete(metadataUrl(recordIdentifier), username, password);

    }

    @Override
    public Validator getValidator(String tag) throws IllegalArgumentException {
        synchronized (validator) {
            Document doc = getSetupDoc();
            try {
                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                XPathExpression expr = xpath.compile(String.format(
                        "//internalValidators/validator[@tag='%s' and @type='simplePutService']", tag));
                NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                if (nodes.getLength() != 1) {
                    throw new IllegalArgumentException(String.format(
                            "No unique internalValidator with tag %s in %s/%s", tag, CONFIGDIR, SETUPFILE));
                }
                String argName = xpath.evaluate("arg/@name", nodes.item(0));
                String argVal = xpath.evaluate("arg/@value", nodes.item(0));
                String argId = String.format("%s:%s", argName, argVal);
                if ("resourceSchemaLocation".equals(argName)) {
                    if (!validator.containsKey(argId)) {
                        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                        Logger.getLogger(DataStoreImpl.class.getName()).info(String.format("fetching schema for %s as %s from %s", tag, argVal, getClass().getResource(argVal)));
                        Schema schema = sf.newSchema(getClass().getResource(argVal));
                        validator.put(argId, new SchemaValidator(schema));
                    }
                } else if ("externalSchemaLocation".equals(argName)) {
                    if (!validator.containsKey(argId)) {
                        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                        Logger.getLogger(DataStoreImpl.class.getName()).info(String.format("fetching schema for %s as %s", tag, argVal));
                        Schema schema = sf.newSchema(new URL(argVal));
                        validator.put(argId, new SchemaValidator(schema));
                    }
                } else if ("webdavSchemaLocation".equals(argName)) {
                    String path = makePath(CONFIGDIR, argVal);
                    // remove validator if too old
                    Date date = getLastModified(path);
                    if (lastDataStoreDate.containsKey(path)) {
                        if (lastDataStoreDate.get(path).before(date)) {
                            validator.remove(argId);
                            Logger.getLogger(DataStoreImpl.class.getName()).info(String.format("schema for %s as %s updated, was %s, new is %s", tag, argVal, lastDataStoreDate.get(path).toString(), date.toString()));
                        }
                    }
                    if (!validator.containsKey(argId)) {
                        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                        Logger.getLogger(DataStoreImpl.class.getName()).info(String.format("fetching schema for %s from %s", tag, path));
                        Schema schema = sf.newSchema(makeURL(CONFIGDIR, argVal));
                        validator.put(argId, new SchemaValidator(schema));
                        lastDataStoreDate.put(path, date);
                    }
                } else {
                    throw new IllegalArgumentException(String.format(
                            "Unknown argument name for validator with tag = %s", tag));
                }
                return validator.get(argId);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            } catch (SAXException e) {
                throw new IllegalArgumentException(e);
            } catch (XPathExpressionException e) {
                Logger.getLogger(DataStoreImpl.class.getName()).log(Level.SEVERE, null, e);
                throw new IllegalArgumentException(String.format("Internal error on handling tag = %s", tag));
            }
        }
    }

    @Override
    public ValidationClient getValidationClient(String recordIdentifier){

        String metadata = readMetadata(recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(getSupportedFormats(), metadata);
        return format.getValidationClient();
    }
}
