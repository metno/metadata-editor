package no.met.metadataeditor.dataTypes;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTemplateFactory {

    
    public static EditorTemplate getInstance(String identifier){

        URL url = EditorTemplateFactory.class.getResource("/defaultConfig/mm2Template.xml");
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
        
        return et;

    }
    
}
