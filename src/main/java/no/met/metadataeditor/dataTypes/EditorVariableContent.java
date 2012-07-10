package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a single content value in an editor variable. The class is
 * recursive so it can contain a mapping of child values as well.
 */
public class EditorVariableContent {

    private DataAttributes attrs;

    private Map<String, List<EditorVariableContent>> children;

    public EditorVariableContent() {
        children = new HashMap<String, List<EditorVariableContent>>();
    }

    public DataAttributes getAttrs() {
        return attrs;
    }

    public void setAttrs(DataAttributes attrs) {
        this.attrs = attrs;
    }

    public void setChildren(Map<String, List<EditorVariableContent>> children) {
        this.children = children;
    }

    public Map<String, List<EditorVariableContent>> getChildren() {
        return children;
    }
}
