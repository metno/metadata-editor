package no.met.metadataeditor.datastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import no.met.metadataeditor.EditorException;

import org.apache.commons.io.FileUtils;

/**
 * DataStore implementation that reads and writes files from the local disk.
 */
public class DiskDataStore extends DataStoreImpl {

    private String basePath;

    public DiskDataStore(String basePath) {
        this.basePath = basePath;
    }

    @Override
    void put(String id, String resource, String username, String password) {

        // IMPLEMENTATION NOTE: We ignore username and password for the DiskDataStore on purpose for simplicity reasons

        File file = new File(id);

        BufferedWriter out = null;
        try {
            // Create file
            FileWriter fstream = new FileWriter(file);
            out = new BufferedWriter(fstream);
            out.write(resource);
        } catch (IOException e) {
            throw new EditorException("Failed to write to file: " + file.getAbsolutePath(), e, EditorException.IO_ERROR);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).severe(e.getMessage());
                }
            }
        }

    }

    @Override
    String get(String id) {
        File file = new File(id);
        StringBuilder sb = new StringBuilder();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new EditorException(e.getMessage(), e, EditorException.IO_ERROR);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).severe(e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    @Override
    Date getLastModified(String id) {
        File file = new File(id);
        return new java.util.Date(file.lastModified());
    }

    @Override
    boolean exists(String id) {
        File file = new File(id);
        return file.exists();
    }

    @Override
    URL makeURL(String... paths) {
        File out = new File(basePath.trim());
        for (String p : paths) {
            out = new File(out, p.trim());
        }
        try {
            return out.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new EditorException(e.getMessage(), e, EditorException.GENERAL_ERROR_CODE);
        }
    }

    @Override
    String makePath(String... paths) {
        URL url = makeURL(paths);
        try {
            return (new File(url.toURI())).toString();
        } catch (URISyntaxException e) {
            // should never happen
            throw new EditorException(e.getMessage(), e, EditorException.GENERAL_ERROR_CODE);
        }
    }

    @Override
    List<String> list(String url){

        //only allow .xml files
        Collection<File> files = FileUtils.listFiles(new File(url), new String[]{"xml"}, false);

        List<String> fileNames = new ArrayList<String>();
        for(File f : files){
            fileNames.add(f.getName());
        }
        return fileNames;
    }

    @Override
    public boolean userHasWriteAccess(String username, String password) {

        // For the disk data store we assume that all user have access to write.
        return true;
    }

    @Override
    public String getDefaultUser(){
        return "";
    }

    @Override
    public String getDefaultPassword(){
        return "";
    }

    @Override
    public boolean delete(String url, String username, String password){
        return FileUtils.deleteQuietly(new File(url));
    }

    @Override
    public List<MetadataRecords.MetadataRecord> listMetadataRecord() {
        //only allow .xml files
        Collection<File> files = FileUtils.listFiles(new File(metadataDirUrl()), new String[]{"xml"}, false);

        List<MetadataRecords.MetadataRecord> records = new ArrayList<>();
        for(File f : files){
            records.add(new MetadataRecords.MetadataRecord(f.getName(), f.getPath(), new Date(f.lastModified())));
        }
        return records;
    }

}
