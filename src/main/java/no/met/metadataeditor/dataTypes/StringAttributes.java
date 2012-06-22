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

    public DataAttributes newInstance() {
        return new StringAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        if ("str".equals(attr)) {
            str = value;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s != str", attr));
        }
    }

    public String getStr() {
        return str;
    }
}
