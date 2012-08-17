package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class UriAttributes extends DataAttributes {
    
    @IsAttributeValue(DataType.URI)
    String uri;

}
