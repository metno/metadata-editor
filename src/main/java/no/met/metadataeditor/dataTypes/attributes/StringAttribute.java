package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class StringAttribute extends DataAttribute {
    
    @IsAttributeValue(DataType.STRING)
    String str;

}
