package no.met.metadataeditor.datastore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

    private final String timeZone = "UTC";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String marshal(Date v) throws Exception {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = utcDate(v);
        return dateFormat.format(date)+"Z";
    }

    private Date utcDate(Date v) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(v);
        Date date =  new Date(calendar.getTimeInMillis());
        return date;
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

}