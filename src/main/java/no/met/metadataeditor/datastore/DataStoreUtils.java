package no.met.metadataeditor.datastore;

import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import no.met.metadataeditor.EditorException;

public class DataStoreUtils {

    
    public static SupportedFormat getFormat(String metadataXML){

        StringReader metadataReader = new StringReader(metadataXML);
        XMLStreamReader reader;
        SupportedFormat format = null;
        try {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(metadataReader);
            while( reader.hasNext() ){
                
                int code = reader.next();
                if( code == XMLStreamConstants.START_ELEMENT ){
                    
                    String tagname = reader.getLocalName();
                    String namespace = reader.getNamespaceURI();
                    
                    if( "mmCombinedMM2".equalsIgnoreCase(tagname) && "http://www.met.no/schema/metamod/mmCombined".equalsIgnoreCase(namespace)){
                        format = SupportedFormat.MM2COMBINED;
                    } else if ( "MM2".equalsIgnoreCase(tagname) && "http://www.met.no/schema/metamod/MM2".equalsIgnoreCase(namespace)) { 
                        format = SupportedFormat.MM2;
                    } else if ( "mmCombinedISO".equalsIgnoreCase(tagname) && "http://www.met.no/schema/metamod/mmCombined".equalsIgnoreCase(namespace)) { 
                        format = SupportedFormat.ISO19139COMBINED;
                    } else if ( "MD_Metadata".equalsIgnoreCase(tagname) && "http://www.isotc211.org/2005/gmd".equalsIgnoreCase(namespace)) { 
                        format = SupportedFormat.ISO19139;
                    } else {

                        throw new IllegalArgumentException("The metadata is not in one of the supported formats");                    
                    }
                    
                    // we are only interested in the first element.
                    break;
                }            
            }            
        } catch (XMLStreamException e) {
            throw new EditorException(e.getMessage());
        } catch (FactoryConfigurationError e) {
            throw new EditorException(e.getMessage());            
        }

        return format;
    }
    
    
    
}
