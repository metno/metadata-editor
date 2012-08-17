package no.met.metadataeditor.dataTypes;


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
