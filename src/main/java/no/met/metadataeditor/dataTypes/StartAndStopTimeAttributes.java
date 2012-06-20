package no.met.metadataeditor.dataTypes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StartAndStopTimeAttributes implements DataAttributes {
    private Date start;
    private Date stop;

    public Map<String, DataType> getFields() {
        Map<String, DataType> fields = new HashMap<String, DataType>();
        fields.put("start", DataType.DATE);
        fields.put("stop", DataType.DATE);
        return fields;
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
