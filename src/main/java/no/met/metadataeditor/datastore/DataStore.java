package no.met.metadataeditor.datastore;

public interface DataStore {

    
    boolean writeMetadata(String project, String recordIdentifier, String xml);
    
    
    String readMetadata(String project, String recordIdentifier);


    String readTemplate(String project, String recordIdentifier);


    String readEditorConfiguration(String project, String recordIdentifier);


    String readResource(String project, String resourceIdentifier);
    
}
