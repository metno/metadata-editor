package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class StartAndStopTimeAttribute extends DataAttribute {

    @IsAttributeValue(DataType.DATETIME)
    String start;

    @IsAttributeValue(DataType.DATETIME)
    String stop;

}
