package no.met.metadataeditor.dataTypes;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class EditorTemplateFactory {

    
    public static EditorTemplate getInstance(String identifier){

        URL url = EditorTemplateFactory.class.getResource("/defaultConfig/mm2Template.xml");
        URL xmlUrl = EditorTemplateFactory.class.getResource("/defaultConfig/exampleMM2.xml");
        EditorTemplate et = null;
        try {
            et = new EditorTemplate(new InputSource(url.openStream()));
            et.addData(new InputSource(xmlUrl.openStream()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }        
        
        return et;

    }
    
}
