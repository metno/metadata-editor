package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;


public class LatLonBBAttribute extends DataAttribute {

    @IsAttributeValue(DataType.FLOAT)
    String south;

    @IsAttributeValue(DataType.FLOAT)
    String north;

    @IsAttributeValue(DataType.FLOAT)
    String east;

    @IsAttributeValue(DataType.FLOAT)
    String west;


}
