package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;


public class LatLonBBAttribute extends DataAttribute {
    
    @IsAttributeValue(DataType.NUMBER)
    String south;

    @IsAttributeValue(DataType.NUMBER)
    String north;
    
    @IsAttributeValue(DataType.NUMBER)
    String east;
    
    @IsAttributeValue(DataType.NUMBER)
    String west;


}
