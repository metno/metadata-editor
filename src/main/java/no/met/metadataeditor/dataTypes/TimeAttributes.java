package no.met.metadataeditor.dataTypes;

public class TimeAttributes extends DataAttributes {

    @IsAttribute(DataType.DATE)
    String time;
    
    public TimeAttributes(){
        
    }
    
    @Override
    public DataAttributes newInstance() {
        return new TimeAttributes();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }    
}
