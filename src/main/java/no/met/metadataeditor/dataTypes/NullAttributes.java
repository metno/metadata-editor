package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.Map;

public class NullAttributes implements DataAttributes {

    public Map<String, DataType> getFields() {
        return new HashMap<String,DataType>();
    }

}
