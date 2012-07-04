package no.met.metadataeditor.datastore;

public enum SupportedFormat {
    
    MM2,
    ISO19139,
    MM2COMBINED,
    ISO19139COMBINED;
    
    
    public String templateName(){
        return this + "Template.xml";
    }
    
    public String editorConfigName(){
        return this + "Editor.xml";
    }
}
