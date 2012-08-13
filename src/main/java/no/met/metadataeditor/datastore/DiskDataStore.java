package no.met.metadataeditor.datastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Date;
import java.util.logging.*;

import no.met.metadataeditor.EditorException;

/**
 * DataStore implementation that reads and writes files from the local disk.
 */
public class DiskDataStore extends DataStoreImpl {

    private String basePath;

    public DiskDataStore(String basePath) {
        this.basePath = basePath;
    }

    @Override
    void put(String id, String resource) {
        File file = new File(id);

        BufferedWriter out = null;
        try {
            // Create file
            FileWriter fstream = new FileWriter(file);
            out = new BufferedWriter(fstream);
            out.write(resource);
        } catch (IOException e) {
            throw new EditorException("Failed to write to file: " + file.getAbsolutePath(), e);
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
            throw new EditorException(e.getMessage());
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
    String makePath(String... paths) {
        File out = new File(basePath);
        for (String p : paths) {
            out = new File(out, p);
        }
        return out.toString();
    }

    @Override
    public boolean userHasWriteAccess(String project, String username, String password) {

        // For the disk data store we assume that all user have access to write.
        return true;
    }

}
