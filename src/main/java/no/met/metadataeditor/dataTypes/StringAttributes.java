package no.met.metadataeditor.dataTypes;

public class StringAttributes extends DataAttributes {
    
    @IsAttribute(DataType.STRING)
    String str;

    public StringAttributes() {}

    public StringAttributes(String str) {
        this.str = str;
    }

    public DataAttributes newInstance() {
        return new StringAttributes();
    }

}
