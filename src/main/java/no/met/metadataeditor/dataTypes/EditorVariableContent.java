package no.met.metadataeditor.dataTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Class used for serialization of the metadata to a JSON format. This class
 * represent a single possibly multivalued editor variable.
 */
public class EditorVariableContent {

    private DataAttributes attrs;

    private Map<String, List<EditorVariable>> children;


    public EditorVariableContent() {
        children = new HashMap<String, List<EditorVariable>>();
    }

    public DataAttributes getAttrs() {
        return attrs;
    }

    public void setAttrs(DataAttributes attrs) {
        this.attrs = attrs;
    }

    public void addChildren(String varName, EditorVariable... vars) {
        children.put(varName, Arrays.asList(vars));
    }

    public Map<String, List<EditorVariable>> getChildren() {
        return children;
    }
}
