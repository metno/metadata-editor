package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class XMDProjectionDataset extends DataAttribute {

    @IsAttributeValue(DataType.STRING)
    String urlRegex;

    @IsAttributeValue(DataType.STRING)
    String urlReplace;

}
