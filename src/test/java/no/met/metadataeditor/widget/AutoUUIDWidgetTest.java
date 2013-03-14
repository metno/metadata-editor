package no.met.metadataeditor.widget;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

import org.junit.Test;

public class AutoUUIDWidgetTest {

    
    @Test
    public void testUUIDGeneration() {
        
        
        List<EditorVariableContent> evcl = new ArrayList<>();
        
        EditorWidget widget = new AutoUUIDWidget();
        widget.setVariableName("test-uuid");
        widget.setMinOccurs(1);
        widget.setMaxOccurs(1);
        widget.setAttributeClass(StringAttribute.class);
                        
        widget.generateWidgetViews(evcl);
        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        
        // will throw exception of it does not work
        UUID.fromString(content.get(0).getAttrs().getAttribute("str"));
    }    

}
