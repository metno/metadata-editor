package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

public class TimeAttributes extends DataAttributes {

    @IsAttribute(DataType.DATE)
    String time;
       
}
