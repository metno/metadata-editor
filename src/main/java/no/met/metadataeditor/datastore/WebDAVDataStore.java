package no.met.metadataeditor.datastore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import no.met.metadataeditor.EditorException;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;



public class WebDAVDataStore extends DataStoreImpl {

    private String host;
    private String protocol;
    private String username;
    private String password;


    public WebDAVDataStore(String protocol, String host, String username, String password) {
        this.protocol = protocol;
        this.host = host;
        this.username = username;
        this.password = password;
    }


    private Sardine getConnection() {
        return SardineFactory.begin(username, password);
    }


    @Override
    void put(String id, String resource, String username, String password) {
        Sardine webdavConn = SardineFactory.begin(username, password);
        try {
            webdavConn.put(id, resource.getBytes());
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to write to WebDAV", e);
            throw new EditorException("Failed to write to WebDAV", e);
        }
    }


    @Override
    String get(String id) {
        Sardine webdavConn = getConnection();

        try {
            InputStream is = webdavConn.get(id);
            return IOUtils.toString(is);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to fetch data from WebDAV: " + id, e);
            throw new EditorException("Failed to fetch metadata from WebDAV", e);
        }
    }

    @Override
    java.util.Date getLastModified(String id) {
        Sardine webdavConn = getConnection();
        try {
            List<DavResource> res = webdavConn.list(id, 0);
            assert (res.size() == 1);
            return res.get(0).getModified();
        } catch (IOException e) {
            Logger.getLogger(WebDAVDataStore.class.getName()).log(Level.SEVERE, "Failed to fetch information from WebDAV: " + id, e);
        }
        return new java.util.Date();
    }


    @Override
    boolean exists(String id) {
        Sardine webdavConn = getConnection();
        try {
            return webdavConn.exists(id);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to connect to WebDAV", e);
        }
        return false;
    }

    @Override
    List<String> list(String url){
        
        Sardine webdavConn = getConnection();
        
        try {
            Collection<DavResource> resources = webdavConn.list(url);
            List<String> filenames = new ArrayList<String>();
            for( DavResource r : resources ){
                if(!r.isDirectory()){
                    filenames.add(r.getName());
                }
            }
            return filenames;
            
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to fetch resource list", e);
            throw new EditorException("Failed to fetch resource list from WebDAV", e);
        }
        
    }    

    @Override
    String makePath(String... paths) {
        StringBuffer fullPath = new StringBuffer();
        fullPath.append(protocol).append("://").append(host);

        for (String p : paths) {
            fullPath.append("/").append(p);
        }
        return fullPath.toString();
    }


    @Override
    public boolean userHasWriteAccess(String username, String password) {

        Sardine webdavConn = SardineFactory.begin(username, password);        
        String accessCheckFile = makePath("checkAccessOk.txt");
        
        try {
            webdavConn.put(accessCheckFile, "Some bytes".getBytes());
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Failed to write to access file", e);
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getDefaultUser(){
        return username;
    }
    
    @Override
    public String getDefaultPassword(){
        return password;
    }
    
    @Override
    public boolean delete(String url, String username, String password){
        
        Sardine webdavConn = SardineFactory.begin(username, password);
        
        if(!exists(url)){
            return false;
        }
        
        try {
            webdavConn.delete(url);
            
            // check if the url actually was deleted
            if(!exists(url)){
                return true;
            } else {
                return false;
            }
            
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to delete resource", e);
            throw new EditorException("Failed to delete resource list from WebDAV", e);            
        }
        
    }

}
