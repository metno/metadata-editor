package no.met.metadataeditor.dataTypes;

import java.net.URI;
import java.net.URISyntaxException;
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

    public DataAttributes newInstance() {
        return new UriAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        URI uri;
        try {
            uri = new URI(attr);
        } catch (URISyntaxException e) {
            throw new AttributesMismatchException(String.format("Attr %s not a URI: %s", attr, e.toString()));
        }
        if ("uri".equals(attr)) {
            this.uri = uri;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s != uri", attr));
        }
    }

    URI getUri() {
        return uri;
    }

    void setUri(URI uri) {
        this.uri = uri;
    }

}
