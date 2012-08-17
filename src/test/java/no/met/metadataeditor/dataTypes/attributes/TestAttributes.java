package no.met.metadataeditor.dataTypes.attributes;

import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;
import no.met.metadataeditor.dataTypes.attributes.DataAttributes;

/**
 * Class used to help testing of the DataAttributes class.
 */
public class TestAttributes extends DataAttributes {

    @IsAttribute(DataType.STRING)
    String val;   
    
    String notExposed;
    
    @Override
    public DataAttributes newInstance() {
        return new TestAttributes();
    }
    
}
