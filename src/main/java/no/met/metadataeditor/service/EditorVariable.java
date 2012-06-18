package no.met.metadataeditor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used for serialization of the metadata to a JSON format. This class
 * represent a single possibly multivalued editor variable.
 */
@XmlRootElement
public class EditorVariable {

    private EditorData attrs;

    private Map<String, List<EditorVariable>> children;

    public EditorVariable() {
        setChildren(new HashMap<String, List<EditorVariable>>());
    }

    public EditorData getAttrs() {
        return attrs;
    }

    public void setAttrs(EditorData attrs) {
        this.attrs = attrs;
    }

    public void addSingleChild(String varName, EditorVariable var) {
        List<EditorVariable> vars = new ArrayList<EditorVariable>();
        vars.add(var);
        children.put(varName, vars);
    }

    public void addChild(String varName, List<EditorVariable> vars) {
        children.put(varName, vars);
    }

    public Map<String, List<EditorVariable>> getChildren() {
        return children;
    }

    public void setChildren(Map<String, List<EditorVariable>> children) {
        this.children = children;
    }

}
