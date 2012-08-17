package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class LatLonBBSingleAttribute extends DataAttributes {

    @IsAttribute(DataType.STRING)
    String latLonStr;

}
