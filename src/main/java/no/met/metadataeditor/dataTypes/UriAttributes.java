package no.met.metadataeditor.dataTypes;

public class UriAttributes extends DataAttributes {
    
    @IsAttribute(DataType.URI)
    String uri;

    public UriAttributes() {
    }

    public UriAttributes(String uri) {
        this.uri = uri;
    }

    public DataAttributes newInstance() {
        return new UriAttributes();
    }

}
