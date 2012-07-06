package no.met.metadataeditor;

import no.met.metadataeditor.EditorConfiguration;
import no.met.metadataeditor.EditorPage;
import no.met.metadataeditor.widget.EditorWidget;
import no.met.metadataeditor.widget.ListWidget;
import no.met.metadataeditor.widget.StringWidget;

import org.junit.Test;
import static org.junit.Assert.*;

import static no.met.metadataeditor.TestHelpers.*;

public class EditorConfigurationFactoryTest {

    
    @Test
    public void testUnmarshalling() {
        
        String configString = fileAsString("/editorconfiguration/config1.xml");
        EditorConfiguration config = EditorConfigurationFactory.unmarshallConfiguration(configString);
        
        assertEquals("Number of pages as expected", 1, config.getPages().size());
        
        EditorPage firstPage = config.getPages().get(0);
        assertEquals("Page name set as expected", "MM2", firstPage.getName());
        
        assertEquals("First page has the correct number of widgets", 2, firstPage.getWidgets().size());
        
        EditorWidget widget1 = firstPage.getWidgets().get(0);
        assertEquals("Widget has the correct type", StringWidget.class, widget1.getClass() );
        
        assertEquals("Widget varname read correctly", "PIName", widget1.getVariableName());
        assertEquals("Widget label read correctly", "PI Name", widget1.getLabel());
        
        EditorWidget widget2 = firstPage.getWidgets().get(1);
        assertEquals("Widget has the correct type", ListWidget.class, widget2.getClass() );
       
    }
    
    
}
