package no.met.metadataeditor.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import no.met.metadataeditor.InvalidEditorConfigurationException;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DateAttribute;
import no.met.metadataeditor.dataTypes.attributes.ListElementAttribute;

import org.junit.Test;

public class NowDateWidgetTest {

    @Test
    public void testMissingNowDateGeneration() {
        
        List<EditorVariableContent> evcl = new ArrayList<>();
        
        EditorWidget widget = new NowDateWidget();
        widget.setVariableName("test-now-date");
        widget.setMinOccurs(1);
        widget.setMaxOccurs(1);
        widget.setAttributeClass(DateAttribute.class);
                        
        widget.generateWidgetViews(evcl);
        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        assertEquals(NowDateWidget.nowTimeStamp(),content.get(0).getAttrs().getAttribute("date"));
    }
    
    @Test
    public void testNewNowDateGeneration() {
        
        List<EditorVariableContent> evcl = new ArrayList<>();
        EditorVariableContent evc = new EditorVariableContent();
        DateAttribute da = new DateAttribute();
        da.addAttribute("date", "2010-01-01");
        evc.setAttrs(da);
        evcl.add(evc);
        
        EditorWidget widget = new NowDateWidget();
        widget.setVariableName("test-now-date");
        widget.setMinOccurs(1);
        widget.setMaxOccurs(1);
        widget.setAttributeClass(DateAttribute.class);
                        
        widget.generateWidgetViews(evcl);
        
        List<EditorVariableContent> content = widget.getContent();
        
        assertEquals(1, content.size());
        
        // NowDateWidget will generate a new date instead of taking the one from editorvariablecontent.
        assertEquals(NowDateWidget.nowTimeStamp(),content.get(0).getAttrs().getAttribute("date"));
    }    
    
    @Test
    public void testValidConfig() {
        
        EditorVariable ev = new EditorVariable(new DateAttribute());
        ev.setMaxOccurs(1);
        ev.setMinOccurs(1);
        
        EditorWidget widget = new NowDateWidget();
        assertTrue(widget.configure(ev));
        
    }
    
    @Test(expected=InvalidEditorConfigurationException.class)
    public void testInvalidMinOccursConfig () {

        EditorVariable ev = new EditorVariable(new DateAttribute());
        ev.setMaxOccurs(1);
        ev.setMinOccurs(0);
        
        EditorWidget widget = new NowDateWidget();
        widget.configure(ev);        

    }    
    
    @Test(expected=InvalidEditorConfigurationException.class)
    public void testInvalidMaxOccursConfig () {

        EditorVariable ev = new EditorVariable(new DateAttribute());
        ev.setMaxOccurs(2);
        ev.setMinOccurs(1);
        
        EditorWidget widget = new NowDateWidget();
        widget.configure(ev);        

    }        
    
    @Test(expected=InvalidEditorConfigurationException.class)
    public void testInvalidDataAttributeConfig () {

        EditorVariable ev = new EditorVariable(new ListElementAttribute());
        ev.setMaxOccurs(1);
        ev.setMinOccurs(1);
        
        EditorWidget widget = new NowDateWidget();
        widget.configure(ev);        

    }      
    
    
}
