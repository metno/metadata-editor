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

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        if ("str".equals(attr)) {
            str = value;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s != str", attr));
        }
    }

    public String getStr() {
        return str;
    }
}
