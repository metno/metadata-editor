package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class TestAttributesSub extends TestAttributes {

    @IsAttribute(DataType.NUMBER)
    String otherAttribute;

}
