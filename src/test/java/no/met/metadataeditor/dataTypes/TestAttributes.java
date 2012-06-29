package no.met.metadataeditor.dataTypes;

import no.met.metadataeditor.dataTypes.DataAttributes;
import no.met.metadataeditor.dataTypes.DataType;
import no.met.metadataeditor.dataTypes.IsAttribute;

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
