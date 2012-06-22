package no.met.metadataeditor.dataTypes;

import java.util.HashMap;
import java.util.Map;

public class LatLonBBAttributes implements DataAttributes {
    private double south;
    private double north;
    private double east;
    private double west;

    public Map<String, DataType> getFields() {
        Map<String, DataType> fields = new HashMap<String, DataType>();
        fields.put("south", DataType.NUMBER);
        fields.put("north", DataType.NUMBER);
        fields.put("east", DataType.NUMBER);
        fields.put("west", DataType.NUMBER);
        return fields;
    }

    public DataAttributes newInstance() {
        return new LatLonBBAttributes();
    }

    public void addAttribute(String attr, String value) throws AttributesMismatchException {
        double val;
        try {
            val = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new AttributesMismatchException(String.format("attr %s: %s", attr, ex.toString()));
        }
        if ("south".equals(attr)) {
            south = val;
        } else if ("north".equals(attr)) {
            north = val;
        } else if ("west".equals(attr)) {
            west = val;
        } else if ("east".equals(attr)) {
            east = val;
        } else {
            throw new AttributesMismatchException(String.format("Attr %s not one of (south|north|east|west, args)", attr));
        }
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

}
