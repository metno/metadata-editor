package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.Map;

public class ListElementAttributes implements DataAttributes {
    String listElement;

    public ListElementAttributes() {
    }
    public ListElementAttributes(String listElement) {
        this.listElement = listElement;
    }

    public Map<String, DataType> getFields() {
        Map<String, DataType> fields = new HashMap<String, DataType>();
        fields.put("listElement", DataType.STRING);
        return fields;
    }

    public DataAttributes newInstance() {
        return new ListElementAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        if ("listElement".equals(attr)) {
            listElement = value;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s != listElement", attr));
        }
    }

    public String getListElement() {
        return listElement;
    }
    public void setListElement(String listElement) {
        this.listElement = listElement;
    }

}
