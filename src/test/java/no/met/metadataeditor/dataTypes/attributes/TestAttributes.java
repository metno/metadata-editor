package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttributeValue;
import no.met.metadataeditor.dataTypes.attributes.DataAttributes;

/**
 * Class used to help testing of the DataAttributes class.
 */
public class TestAttributes extends DataAttributes {

    @IsAttributeValue(DataType.STRING)
    String val;   
    
    String notExposed;
    
}
