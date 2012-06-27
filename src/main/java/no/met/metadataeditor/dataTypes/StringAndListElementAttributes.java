package no.met.metadataeditor.dataTypes;

public class StringAndListElementAttributes extends DataAttributes {
    
    @IsAttribute(DataType.STRING)
    private String str;
    
    @IsAttribute(DataType.STRING)
    private String listElement;

    public StringAndListElementAttributes() {
    }
    public StringAndListElementAttributes(String str, String listElement) {
        this.setStr(str);
        this.setListElement(listElement);
    }

    public DataAttributes newInstance() {
        return new StringAndListElementAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        if ("listElement".equals(attr)) {
            listElement = value;
        } else if ("str".equals(attr)) {
            str = value;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s not one of (str|listElement)", attr));
        }
    }


    public String getStr() {
        return str;
    }
    public void setStr(String str) {
        this.str = str;
    }
    public String getListElement() {
        return listElement;
    }
    public void setListElement(String listElement) {
        this.listElement = listElement;
    }

}
