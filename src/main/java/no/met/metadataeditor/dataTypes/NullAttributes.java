package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.Map;

public class NullAttributes implements DataAttributes {

    public Map<String, DataType> getFields() {
        return new HashMap<String,DataType>();
    }

    public DataAttributes newInstance() {
        return new NullAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        throw new AttributesMismatchException("no attributes allowed");
    }
}
