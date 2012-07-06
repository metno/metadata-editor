package no.met.metadataeditor.datastore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.*;

import no.met.metadataeditor.EditorException;

public class DiskDataStore implements DataStore {

    private String basePath;

    public DiskDataStore(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean writeMetadata(String project, String recordIdentifier, String xml) {

        File metadataFile = metadataPath(project, recordIdentifier);

        BufferedWriter out = null;
        try {
            // Create file
            FileWriter fstream = new FileWriter(metadataFile);
            out = new BufferedWriter(fstream);
            out.write(xml);
        } catch (IOException e) {
            throw new EditorException("Failed to write to file: " + metadataFile.getAbsolutePath(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).severe(e.getMessage());
                }
            }
        }
        
        return true;

    }

    @Override
    public String readMetadata(String project, String recordIdentifier) {

        File metadataFile = metadataPath(project, recordIdentifier);
        return readFile(metadataFile);
    }

    @Override
    public String readTemplate(String project, String recordIdentifier) {

        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        File templatePath = templatePath(project, format);

        if (!templatePath.exists()) {
            throw new EditorException("File does not exist: " + templatePath.getAbsolutePath());
        }

        return readFile(templatePath);
    }

    @Override
    public String readEditorConfiguration(String project, String recordIdentifier) {
        
        String metadata = readMetadata(project, recordIdentifier);

        SupportedFormat format = DataStoreUtils.getFormat(metadata);
        
        File configPath = configurationPath(project, format);
        if(!configPath.exists()){
            throw new EditorException("No configuration exists for the format: " + format );
        }
        
        return readFile(configPath);
    }

    @Override
    public String readResource(String project, String resourceIdentifier) {
        
        File resourcePath = resourcePath(project, resourceIdentifier);
        if( !resourcePath.exists()){
            throw new EditorException("Resource file does not exist: " + resourcePath.getAbsolutePath());
        }
        
        return readFile(resourcePath);
    }

    
    private File configurationPath(String project, SupportedFormat format){
        File dir = new File(new File(basePath, project), "config");
        File path = new File(dir, format.editorConfigName());
        return path;
    }
    
    private File resourcePath(String project, String resourceIdentifier){
        
        File dir = new File(basePath, project);
        File path = new File(dir, resourceIdentifier);
        return path;
    }
    
    private File metadataPath(String project, String recordIdentifier) {

        File dir = new File(new File(basePath, project), "XML");
        File path = new File(dir, recordIdentifier + ".xml");
        return path;

    }

    private File templatePath(String project, SupportedFormat format) {
        File dir = new File(new File(basePath, project), "config");
        File path = new File(dir, format.templateName());
        return path;
    }

    private String readFile(File file) {

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

}
