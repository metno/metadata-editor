package no.met.metadataeditor.widget;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

import org.junit.Test;

public class MetnoDatasetIdentifierWidgetTest {

    @Test
    public void testEmptyWidgetValue() {
        
        List<EditorVariableContent> evcl = createVariableContent("");
        EditorWidget widget = createWidget();                        
        widget.generateWidgetViews(evcl);        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        assertEquals("",content.get(0).getAttrs().getAttribute("str"));
    }   
    
    @Test
    public void testNewWidgetValue() {
        
        List<EditorVariableContent> evcl = createVariableContent("hirlam-8km");
        EditorWidget widget = createWidget();                        
        widget.generateWidgetViews(evcl);        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        assertThat(content.get(0).getAttrs().getAttribute("str"),startsWith("hirlam-8km-a-"));
    }  
    
    @Test
    public void testOldValueValue() {
        
        List<EditorVariableContent> evcl = createVariableContent("hirlam-8km-a-mk8c77a");
        EditorWidget widget = createWidget();                        
        widget.generateWidgetViews(evcl);        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        assertEquals("hirlam-8km-a-mk8c77a",content.get(0).getAttrs().getAttribute("str"));
    }  
    
    
    private MetnoDatasetIdentifierWidget createWidget(){
        MetnoDatasetIdentifierWidget widget = new MetnoDatasetIdentifierWidget("a");
        widget.setVariableName("test-now-date");
        widget.setMinOccurs(1);
        widget.setMaxOccurs(1);
        widget.setAttributeClass(StringAttribute.class);
        return widget;
    }
    
    private List<EditorVariableContent> createVariableContent(String value){
        List<EditorVariableContent> evcl = new ArrayList<>();
        EditorVariableContent evc = new EditorVariableContent();
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", value);
        evc.setAttrs(sa);
        evcl.add(evc);
        return evcl;
    }

}
