package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class UriAttribute extends DataAttribute {
    
    @IsAttributeValue(DataType.URI)
    String uri;

}
