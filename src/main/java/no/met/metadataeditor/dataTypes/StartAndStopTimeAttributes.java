package no.met.metadataeditor.dataTypes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartAndStopTimeAttributes extends DataAttributes {
    
    @IsAttribute(DataType.DATE)
    private Date start;
    
    @IsAttribute(DataType.DATE)
    private Date stop;

    public DataAttributes newInstance() {
        return new StartAndStopTimeAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = (Date)formatter.parse(value);
        } catch (ParseException e) {
            throw new AttributesMismatchException(String.format("Attr-value for %s not a date: %s", attr, e.toString()));
        }
        if ("start".equals(attr)) {
            start = date;
        } else if ("stop".equals(attr)) {
            stop = date;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s not one of (start|stop))", attr));
        }
    }

    Date getStart() {
        return start;
    }

    void setStart(Date start) {
        this.start = start;
    }

    Date getStop() {
        return stop;
    }

    void setStop(Date stop) {
        this.stop = stop;
    }

}
