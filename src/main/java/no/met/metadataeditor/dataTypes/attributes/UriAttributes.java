package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class UriAttributes extends DataAttributes {
    
    @IsAttribute(DataType.URI)
    String uri;

}
