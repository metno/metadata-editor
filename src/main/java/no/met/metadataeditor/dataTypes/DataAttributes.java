package no.met.metadataeditor.dataTypes;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface class that is only used as a type placeholder in the
 * EditorVariable class.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")  
public interface DataAttributes {

    @JsonIgnore
    public Map<String, DataType> getFields();
}
