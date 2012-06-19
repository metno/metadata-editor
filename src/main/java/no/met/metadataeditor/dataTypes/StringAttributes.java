package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.Map;

public class StringAttributes implements DataAttributes {
    String str;

    public StringAttributes() {}

    public StringAttributes(String str) {
        this.str = str;
    }

    public Map<String, DataType> getFields() {
        Map<String, DataType> fields = new HashMap<String, DataType>();
        fields.put("str", DataType.STRING);
        return fields;
    }

    public String getStr() {
        return str;
    }
}
