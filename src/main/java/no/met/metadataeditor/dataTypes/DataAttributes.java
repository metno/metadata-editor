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

    /**
     *
     * @return a new Instance of the subtype
     */
    public DataAttributes newInstance();

    /**
     * add a attributes value by a string
     * @param string
     * @param value
     * @throws AttributesMismatchException if attr does not exists or value
     *         does not deserialize to the DataType
     */
    public void addAttribute(String attr, String value) throws AttributesMismatchException;
}
