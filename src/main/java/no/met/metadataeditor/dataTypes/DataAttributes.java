package no.met.metadataeditor.dataTypes;

import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Interface class that is only used as a type placeholder in the
 * EditorVariable class.
 */
public interface DataAttributes {

    @XmlTransient
    public Map<String, DataType> getFields();
}
