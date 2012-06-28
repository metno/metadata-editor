package no.met.metadataeditor.dataTypes;

public class ListElementAttributes extends DataAttributes {
    
    @IsAttribute(DataType.STRING)
    String listElement;

    public ListElementAttributes() {
    }
    public ListElementAttributes(String listElement) {
        this.listElement = listElement;
    }

    public DataAttributes newInstance() {
        return new ListElementAttributes();
    }
}
