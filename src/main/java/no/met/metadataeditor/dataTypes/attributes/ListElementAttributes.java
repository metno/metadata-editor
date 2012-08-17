package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class ListElementAttributes extends DataAttributes {
    
    @IsAttributeValue(DataType.STRING)
    String listElement;

}
