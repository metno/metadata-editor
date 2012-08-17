package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.met.metadataeditor.dataTypes.attributes.DataAttribute;

/**
 * Class representing a single content value in an editor variable. The class is
 * recursive so it can contain a mapping of child values as well.
 */
public class EditorVariableContent {

    private DataAttribute attrs;

    private Map<String, List<EditorVariableContent>> children;

    public EditorVariableContent() {
        children = new HashMap<String, List<EditorVariableContent>>();
    }

    public DataAttribute getAttrs() {
        return attrs;
    }

    public void setAttrs(DataAttribute attrs) {
        this.attrs = attrs;
    }

    public void setChildren(Map<String, List<EditorVariableContent>> children) {
        this.children = children;
    }

    public Map<String, List<EditorVariableContent>> getChildren() {
        return children;
    }
}
