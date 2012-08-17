package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;


public class LatLonBBAttributes extends DataAttributes {
    
    @IsAttribute(DataType.NUMBER)
    String south;

    @IsAttribute(DataType.NUMBER)
    String north;
    
    @IsAttribute(DataType.NUMBER)
    String east;
    
    @IsAttribute(DataType.NUMBER)
    String west;


}
