package no.met.metadataeditor.dataTypes;

public class NullAttributes extends DataAttributes {

    public DataAttributes newInstance() {
        return new NullAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        throw new AttributesMismatchException("no attributes allowed");
    }
}
