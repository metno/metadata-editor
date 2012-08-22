package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class KeyValueListAttribute extends DataAttribute {

    @IsAttributeValue(DataType.STRING)
    String key;
    
    @IsAttributeValue(DataType.STRING)
    String value;
    
    
}
