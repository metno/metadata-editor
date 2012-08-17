package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class TimeAttributes extends DataAttributes {

    @IsAttributeValue(DataType.DATE)
    String time;
       
}
