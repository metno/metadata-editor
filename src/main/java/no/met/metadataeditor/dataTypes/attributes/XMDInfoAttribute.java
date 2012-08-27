package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class XMDInfoAttribute extends DataAttribute {

    @IsAttributeValue(DataType.STRING)
    String name;

    @IsAttributeValue(DataType.STRING)
    String status;

    @IsAttributeValue(DataType.DATETIME)
    String creation;

    @IsAttributeValue(DataType.DATETIME)
    String lastChanged;

    @IsAttributeValue(DataType.STRING)
    String ownertag;

}
