package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class StartAndStopTimeAttributes extends DataAttributes {
    
    @IsAttribute(DataType.DATE)
    String start;
    
    @IsAttribute(DataType.DATE)
    String stop;

}
