package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class XMDProjectionFimex extends DataAttribute {

    @IsAttributeValue(DataType.STRING)
    String name;

    @IsAttributeValue(DataType.STRING)
    String method;

    @IsAttributeValue(DataType.STRING)
    String projString;

    @IsAttributeValue(DataType.STRING)
    String xAxis;

    @IsAttributeValue(DataType.STRING)
    String yAxis;

    @IsAttributeValue(DataType.STRING)
    String toDegree;

}
