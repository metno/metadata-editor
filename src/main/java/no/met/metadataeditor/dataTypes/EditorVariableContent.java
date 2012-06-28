package no.met.metadataeditor.dataTypes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



/**
 * Class used for serialization of the metadata to a JSON format. This class
 * represent a single possibly multivalued editor variable.
 */
public class EditorVariableContent implements Serializable {

    private static final long serialVersionUID = -5518951323575863694L;

    private DataAttributes attrs;

    private Map<String, EditorVariable> children;


    public EditorVariableContent() {
        children = new HashMap<String, EditorVariable>();
    }

    public DataAttributes getAttrs() {
        return attrs;
    }

    public void setAttrs(DataAttributes attrs) {
        this.attrs = attrs;
    }

    public void setChildren(Map<String, EditorVariable> children) {
        this.children = children;
    }

    public Map<String, EditorVariable> getChildren() {
        return children;
    }
}
