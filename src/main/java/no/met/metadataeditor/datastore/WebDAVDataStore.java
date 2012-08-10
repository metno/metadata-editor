package no.met.metadataeditor.datastore;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import no.met.metadataeditor.EditorException;

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
    void put(String id, String resource) {
        Sardine webdavConn = getConnection();
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
    String makePath(String... paths) {
        StringBuffer fullPath = new StringBuffer();
        fullPath.append(protocol).append("://").append(host);

        for (String p : paths) {
            fullPath.append("/").append(p);
        }
        return fullPath.toString();
    }

}
