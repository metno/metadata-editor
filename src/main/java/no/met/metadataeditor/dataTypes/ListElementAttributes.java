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

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        if ("listElement".equals(attr)) {
            listElement = value;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s != listElement", attr));
        }
    }

    public String getListElement() {
        return listElement;
    }
    public void setListElement(String listElement) {
        this.listElement = listElement;
    }

}
