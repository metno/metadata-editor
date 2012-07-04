package no.met.metadataeditor.dataTypes;

public class LatLonBBSingleAttribute extends DataAttributes {

    @IsAttribute(DataType.STRING)
    String latLonStr;
    
    @Override
    public DataAttributes newInstance() {
        return new LatLonBBSingleAttribute();
    }

}
