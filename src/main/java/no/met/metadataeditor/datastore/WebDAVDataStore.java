package no.met.metadataeditor.datastore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.met.metadataeditor.EditorException;

import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;



public class WebDAVDataStore implements DataStore {

    private String host;
    
    private String protocol;
    
    public WebDAVDataStore(String protocol, String host){
        this.protocol = protocol;
        this.host = host;
    }
    
    
    @Override
    public boolean writeMetadata(String project, String recordIdentifier, String xml) {
        
        String url = metadataUrl(project, recordIdentifier);
        
        Sardine webdavConn = getConnection();
        try {
            webdavConn.put(url, xml.getBytes());
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to write to WebDAV", e);
            throw new EditorException("Failed to write to WebDAV", e);
        }
        
        return true;
    }

    @Override
    public boolean projectExists(String project) {
        return urlExists(webdavUrl(project));
    }

    @Override
    public boolean metadataExists(String project, String recordIdentifier) {
        return urlExists(metadataUrl(project, recordIdentifier));
    }
    
    protected boolean urlExists(String url){

        Sardine webdavConn = getConnection();
        try {
            return webdavConn.exists(url);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to connect to WebDAV", e);
        }
        return false;
    }

    protected String urlContents(String url){

        Sardine webdavConn = getConnection();
        
        try {
            InputStream is = webdavConn.get(url);
            return convertStreamToString(is);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to fetch metadata from WebDAV", e);
            throw new EditorException("Failed to fetch metadata from WebDAV", e);
        }        
        
    }
    
    @Override
    public String readMetadata(String project, String recordIdentifier) {
        
        return urlContents(metadataUrl(project, recordIdentifier));
    }

    @Override
    public String readTemplate(String project, String recordIdentifier) {
        
        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        String templateUrl = templateUrl(project, format);

        if (!urlExists(templateUrl)) {
            throw new EditorException("Template does not exist: " + templateUrl);
        }        
        
        return urlContents(templateUrl);
    }

    @Override
    public String readEditorConfiguration(String project, String recordIdentifier) {

        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        String configurationUrl = configurationUrl(project, format);

        if (!urlExists(configurationUrl)) {
            throw new EditorException("Template does not exist: " + configurationUrl);
        }        
        
        return urlContents(configurationUrl);        
        
    }

    @Override
    public String readResource(String project, String resourceIdentifier) {

        String resourceUrl = resourceUrl(project, resourceIdentifier);

        if (!urlExists(resourceUrl)) {
            throw new EditorException("Resource does not exist: " + resourceUrl);
        }                
        return urlContents(resourceUrl);                
    }
    
    private Sardine getConnection() {
        return SardineFactory.begin();
    }

    
    private String templateUrl(String project, SupportedFormat format) {
        return webdavUrl(project, "config", format.templateName());
    }    
    
    private String configurationUrl(String project, SupportedFormat format){
        
        return webdavUrl(project, "config", format.editorConfigName());
    }
    
    private String resourceUrl(String project, String resourceIdentifier){
        
        return webdavUrl(project, resourceIdentifier);
    }
    
    private String metadataUrl(String project, String recordIdentifier) {

        return webdavUrl(project, "XML", recordIdentifier + ".xml");

    }    
    
    protected String webdavUrl(String... paths){
        
        File fullPath = new File(host);
        
        for( int i = 0; i < paths.length; i++ ){
            fullPath = new File(fullPath, paths[i]);
        }
        return protocol + "://" + fullPath.toString();
        
    }   
    
    String convertStreamToString(InputStream is) {
        
        // tokenize from the beginning of the string with \A. See for more details http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html        
        return new java.util.Scanner(is).useDelimiter("\\A").next();
    }    

}
