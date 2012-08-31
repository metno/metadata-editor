package no.met.metadataeditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("no.met.metadataeditor.DateConverter")
public class DateConverter implements Converter {

    public static final String FROM_UI_FORMAT = "yyyy-MM-dd";

    public static final String FROM_XML_FORMAT = "yyyy-MM-dd";

    private static DateFormat fromUIFormat = new SimpleDateFormat(FROM_UI_FORMAT);
    private static DateFormat fromXMLFormat = new SimpleDateFormat(FROM_XML_FORMAT);


    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String dateString) throws ConverterException {

      try {
          Date uiDate = fromUIFormat.parse(dateString);
          return fromXMLFormat.format(uiDate);

      } catch (ParseException e) {
          return dateString;
      }

    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object value) throws ConverterException {

        if( value instanceof String){
            String dateString = (String) value;

            try {
                Date uiDate = fromXMLFormat.parse(dateString);
                return fromUIFormat.format(uiDate);

            } catch (ParseException e) {
                return dateString;
            }
        }

        return value.toString();

    }

}
