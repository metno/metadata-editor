package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;

public class DateAttribute extends DataAttribute {

    @IsAttributeValue(DataType.DATE)
    String date;

}
