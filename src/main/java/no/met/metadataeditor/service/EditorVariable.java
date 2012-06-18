package no.met.metadataeditor.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EditorVariable {
    private Map<String, URI> resources = new HashMap<String, URI>();
    private List<EditorVariableContent> content = new ArrayList<EditorVariableContent>();

    public EditorVariable() {
    }

    public Map<String, URI> getResources() {
        return resources;
    }
    public void addResource(String name, URI uri) {
        resources.put(name, uri);
    }

    public void addContent(EditorVariableContent content) {
        this.content.add(content);
    }
    public List<EditorVariableContent> getContent() {
        return content;
    }

}
