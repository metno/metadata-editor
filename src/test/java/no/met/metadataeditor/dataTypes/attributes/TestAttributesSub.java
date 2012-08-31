package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class TestAttributesSub extends TestAttributes {

    @IsAttributeValue(DataType.FLOAT)
    String otherAttribute;

}
