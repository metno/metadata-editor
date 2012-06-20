package no.met.metadataeditor.dataTypes;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UriAttributes implements DataAttributes {
    private URI uri;

    public UriAttributes() {
    }

    public UriAttributes(URI uri) {
        this.setUri(uri);
    }

    public Map<String, DataType> getFields() {
        Map<String, DataType> fields = new HashMap<String, DataType>();
        fields.put("uri", DataType.URI);
        return fields;
    }

    URI getUri() {
        return uri;
    }

    void setUri(URI uri) {
        this.uri = uri;
    }

}
