package no.met.metadataeditor.dataTypes;

public class StringAndListElementAttributes extends DataAttributes {
    
    @IsAttribute(DataType.STRING)
    String str;
    
    @IsAttribute(DataType.STRING)
    String listElement;

    public StringAndListElementAttributes() {
    }

    public DataAttributes newInstance() {
        return new StringAndListElementAttributes();
    }

}