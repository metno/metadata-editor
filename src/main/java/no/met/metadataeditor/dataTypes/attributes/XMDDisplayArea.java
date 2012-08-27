package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class XMDDisplayArea extends DataAttribute {

    @IsAttributeValue(DataType.URI)
    String crs;

    @IsAttributeValue(DataType.INTEGER)
    String left;

    @IsAttributeValue(DataType.INTEGER)
    String right;

    @IsAttributeValue(DataType.INTEGER)
    String top;

    @IsAttributeValue(DataType.INTEGER)
    String bottom;
}
