package no.met.metadataeditor.datastore;

public interface DataStore {

    
    boolean writeMetadata(String project, String recordIdentifier, String xml);
    
    boolean projectExists(String project);
    
    boolean metadataExists(String project, String recordIdentifier);
    
    String readMetadata(String project, String recordIdentifier);


    String readTemplate(String project, String recordIdentifier);


    String readEditorConfiguration(String project, String recordIdentifier);


    String readResource(String project, String resourceIdentifier);
    
}
