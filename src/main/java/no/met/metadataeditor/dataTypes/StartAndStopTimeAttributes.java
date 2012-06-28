package no.met.metadataeditor.dataTypes;

public class StartAndStopTimeAttributes extends DataAttributes {
    
    @IsAttribute(DataType.DATE)
    String start;
    
    @IsAttribute(DataType.DATE)
    String stop;

    public DataAttributes newInstance() {
        return new StartAndStopTimeAttributes();
    }

}
