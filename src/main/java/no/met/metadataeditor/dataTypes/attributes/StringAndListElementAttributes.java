package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class StringAndListElementAttributes extends DataAttributes {
    
    @IsAttribute(DataType.STRING)
    String str;
    
    @IsAttribute(DataType.STRING)
    String listElement;

}
