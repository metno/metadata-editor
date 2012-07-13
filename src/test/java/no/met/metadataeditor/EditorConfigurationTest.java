package no.met.metadataeditor;

import static no.met.metadataeditor.TestHelpers.*;

import java.util.Map;

import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.widget.EditorWidget;

import org.junit.Test;
import static org.junit.Assert.*;

public class EditorConfigurationTest {

    
    @Test
    public void testNoChildrenConfigure() {
        
        EditorConfiguration config = getConfiguration("/editorconfiguration/noChildrenConfig.xml");
        Map<String, EditorVariable> varMap = getVariables("/editorconfiguration/noChildrenTemplate.xml");
        config.configure(varMap);
        
        EditorWidget keywordWidget = config.getPage("MM2").getWidget("keywords");
        
        assertEquals("maxOccurs set from template", 10, keywordWidget.getMaxOccurs());
        assertEquals("minOccurs set from template", 4, keywordWidget.getMinOccurs());
        assertEquals("maxOccurs set from template", "test.txt", keywordWidget.getResourceUri().toString());
    }

    @Test
    public void testChildConfigure() {
        
        EditorConfiguration config = getConfiguration("/editorconfiguration/childrenConfig.xml");
        Map<String, EditorVariable> varMap = getVariables("/editorconfiguration/childrenTemplate.xml");
        config.configure(varMap);
        
        EditorWidget keywordWidget = config.getPage("MM2").getWidget("keywords");

        assertEquals("minOccurs set from template", 4, keywordWidget.getMinOccurs());
        assertEquals("maxOccurs set from template", 10, keywordWidget.getMaxOccurs());
        assertEquals("maxOccurs set from template", "test.txt", keywordWidget.getResourceUri().toString());
        
        EditorWidget child1Widget = keywordWidget.getChildren().get(0);
        assertEquals("minOccurs set from template in child1", 2, child1Widget.getMinOccurs());        
        assertEquals("maxOccurs set from template in child1", 5, child1Widget.getMaxOccurs());
        assertEquals("maxOccurs set from template in child1", null, child1Widget.getResourceUri());
        
        EditorWidget child2Widget = keywordWidget.getChildren().get(1);
        assertEquals("minOccurs set from template in child1", 0, child2Widget.getMinOccurs());        
        assertEquals("maxOccurs set from template in child1", 1, child2Widget.getMaxOccurs());
        assertEquals("maxOccurs set from template in child1", "child.txt", child2Widget.getResourceUri().toString());          
    }

}
