package no.met.metadataeditor.datastore;


/**
 * Enum of supported XML metadata formats.
 */
public class SupportedFormat {
   
    SupportedFormat(String tag, String rootNode, String namespace) {
        this.tag = tag;
        this.rootNode = rootNode;
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
    public String getRootNode() {
        return rootNode;
    }
    public String getTagName() {
        return tag;
    }


    public String templateName(){
        return tag + "Template.xml";
    }

    public String editorConfigName(){
        return tag + "Editor.xml";
    }

    /**
     * Check if rootNode and namespace are identical to this format.
     *
     * @param rootNode
     * @param namespace
     */
    public boolean matches(String rootNode, String namespace) {
        if (this.rootNode.equals(rootNode)) {
            if (this.namespace == null) {
                if (namespace == null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return this.namespace.equals(namespace);
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o){
        
        if( !( o instanceof SupportedFormat ) ){
            return false;
        }
        
        SupportedFormat f = (SupportedFormat) o;
        
        return this.tag.equals(f.tag) && this.namespace.equals(f.namespace) && this.rootNode.endsWith(f.rootNode);
        
    }
    
    public int hashCode(){
        
        int result = 17;
        result = 31 * result + tag.hashCode();
        result = 31 * result + namespace.hashCode();
        result = 31 * result + rootNode.hashCode();
        
        return result;
        
    }
   
    private String tag;
    private String rootNode;
    private String namespace;
}
