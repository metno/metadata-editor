package no.met.metadataeditor.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.met.metadataeditor.EditorWidgetView;
import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;

import org.junit.Test;

public class EditorWidgetTest {

    
    @Test
    public void testGenerateWidgetViewSingleValue() {
        
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", "dummy");
        
        List<EditorVariableContent> evcl = createEditorVariableContent(sa);
        
        EditorWidget ew = generateStringWidget("some_var", 1, 1);        
        ew.generateWidgetViews(evcl);
        
        List<EditorWidgetView> ewv = ew.getWidgetViews();
        
        assertEquals(1, ewv.size());
        assertEquals(ewv.get(0).valuesAsAttriubte(), sa);
        
    }

    @Test
    public void testGenerateWidgetViewMultiValue() {
        
        StringAttribute sa1 = new StringAttribute();
        sa1.addAttribute("str", "dummy1");
        StringAttribute sa2 = new StringAttribute();
        sa2.addAttribute("str", "dummy2");
        StringAttribute sa3 = new StringAttribute();
        sa3.addAttribute("str", "dummy3");
        
        List<EditorVariableContent> evcl = createEditorVariableContent(sa1, sa2, sa3);
        
        EditorWidget ew = generateStringWidget("some_var", 1, 1);        
        ew.generateWidgetViews(evcl);
        
        List<EditorWidgetView> ewv = ew.getWidgetViews();
        
        assertEquals(3, ewv.size());
        assertEquals(ewv.get(0).valuesAsAttriubte(), sa1);
        assertEquals(ewv.get(1).valuesAsAttriubte(), sa2);
        assertEquals(ewv.get(2).valuesAsAttriubte(), sa3);
        
    }
    
    @Test
    public void testGenerateWidgetViewMinOccurs() {
        
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", "dummy1");
        StringAttribute created = new StringAttribute();
        created.addAttribute("str", "");

        List<EditorVariableContent> evcl = createEditorVariableContent(sa);
        
        EditorWidget ew = generateStringWidget("some_var", 2, 2);        
        ew.generateWidgetViews(evcl);
        
        List<EditorWidgetView> ewv = ew.getWidgetViews();
        
        assertEquals(2, ewv.size());
        assertEquals(ewv.get(0).valuesAsAttriubte(), sa);
        assertEquals(ewv.get(1).valuesAsAttriubte(), created);
        
    }    
    
    @Test
    public void testGenerateWidgetViewChildren() {
        
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", "dummy1");
        StringAttribute childSa1 = new StringAttribute();
        childSa1.addAttribute("str", "dummy2");
        StringAttribute childSa2 = new StringAttribute();
        childSa2.addAttribute("str", "dummy3");        
        StringAttribute createdSa = new StringAttribute();
        createdSa.addAttribute("str", "");        
        
        List<EditorVariableContent> evcl = createEditorVariableContent(sa);
        addChildVariableContent(evcl.get(0), "child1", childSa1);
        addChildVariableContent(evcl.get(0), "child2", childSa2);
        
        EditorWidget child1 = generateStringWidget("child1", 0, 1);
        EditorWidget child2 = generateStringWidget("child2", 4, 4);        
        EditorWidget ew = generateStringWidget("some_var", 1, 1, child1, child2);
       
        ew.generateWidgetViews(evcl);        
        List<EditorWidgetView> ewv = ew.getWidgetViews();
        
        assertEquals(1, ewv.size());
        assertEquals(ewv.get(0).valuesAsAttriubte(), sa);

        assertEquals(1, ewv.get(0).getChildren().get(0).getWidgetViews().size());
        assertEquals(childSa1, ewv.get(0).getChildren().get(0).getWidgetViews().get(0).valuesAsAttriubte());

        assertEquals(4, ewv.get(0).getChildren().get(1).getWidgetViews().size());
        assertEquals(childSa2, ewv.get(0).getChildren().get(1).getWidgetViews().get(0).valuesAsAttriubte());
        assertEquals(createdSa, ewv.get(0).getChildren().get(1).getWidgetViews().get(1).valuesAsAttriubte());
        assertEquals(createdSa, ewv.get(0).getChildren().get(1).getWidgetViews().get(2).valuesAsAttriubte());
        assertEquals(createdSa, ewv.get(0).getChildren().get(1).getWidgetViews().get(3).valuesAsAttriubte());
        
    }
    
    @Test
    public void testGetContentSingleValue(){
        
        EditorWidget ew = generateStringWidget("test", 0, 1);
        addStringWidgetViews(ew, "dummy123");
                
        List<EditorVariableContent> evcl = ew.getContent();
        
        assertEquals(1, evcl.size());
        assertEquals("dummy123", evcl.get(0).getAttrs().getAttribute("str") );
    }
    
    @Test
    public void testGetContentMultiValue(){
        
        EditorWidget ew = generateStringWidget("test", 0, 5);
        addStringWidgetViews(ew, "dummy123");
        addStringWidgetViews(ew, "dummy321");
        addStringWidgetViews(ew, "dummy333");
                
        List<EditorVariableContent> evcl = ew.getContent();
        
        assertEquals(3, evcl.size());
        assertEquals("dummy123", evcl.get(0).getAttrs().getAttribute("str") );
        assertEquals("dummy321", evcl.get(1).getAttrs().getAttribute("str") );
        assertEquals("dummy333", evcl.get(2).getAttrs().getAttribute("str") );

    }
    
    @Test
    public void testGetContentWithChildren(){
        
        EditorWidget child1 = generateStringWidget("child1", 0, 1);
        EditorWidget child2 = generateStringWidget("child2", 0, 3);               
        EditorWidget ew = generateStringWidget("test", 0, 1, child1, child2);
        
        EditorWidgetView parentEwv = addStringWidgetViews(ew, "parent");
        
        // widget views need to be attached to the widgets in the parent's editor widget view
        addStringWidgetViews(parentEwv.getChildren().get(0), "child1 value");        
        addStringWidgetViews(parentEwv.getChildren().get(1), "child2_1");
        addStringWidgetViews(parentEwv.getChildren().get(1), "child2_2");
        addStringWidgetViews(parentEwv.getChildren().get(1), "child2_3");

        EditorVariable evParent = new EditorVariable(new StringAttribute());
        EditorVariable evChild1 = new EditorVariable(new StringAttribute());
        EditorVariable evChild2 = new EditorVariable(new StringAttribute());
        evParent.addChild("child1", evChild1);
        evParent.addChild("child2", evChild2);
        List<EditorVariableContent> evcl = ew.getContent();
        
        assertEquals(1, evcl.size());
        assertEquals("parent", evcl.get(0).getAttrs().getAttribute("str") );
        
        Map<String, List<EditorVariableContent>> childContent = evcl.get(0).getChildren();
        
        assertTrue(childContent.containsKey("child1"));
        assertEquals(1, childContent.get("child1").size());
        assertEquals("child1 value", childContent.get("child1").get(0).getAttrs().getAttribute("str"));
        
        assertTrue(childContent.containsKey("child2"));
        assertEquals(3, childContent.get("child2").size());
        assertEquals("child2_1", childContent.get("child2").get(0).getAttrs().getAttribute("str"));
        assertEquals("child2_2", childContent.get("child2").get(1).getAttrs().getAttribute("str"));
        assertEquals("child2_3", childContent.get("child2").get(2).getAttrs().getAttribute("str"));

    }    
    
    private List<EditorVariableContent> createEditorVariableContent(DataAttribute... attributes){
        
        List<EditorVariableContent> evcl = new ArrayList<>();
        for( DataAttribute attr : attributes ){
            EditorVariableContent evc = new EditorVariableContent();
            evc.setAttrs(attr);
            evcl.add(evc);
        }
        return evcl;        
    }
    
    private void addChildVariableContent(EditorVariableContent parentContent, String name, DataAttribute... childAttributes){
        
        List<EditorVariableContent> childContent = createEditorVariableContent(childAttributes);
        parentContent.addChild(name, childContent);
        
        
    }
    
    private EditorWidgetView addStringWidgetViews(EditorWidget widget, String attrValue){
        
        Map<String, String> values = new HashMap<>();
        values.put("str", attrValue);
        EditorWidgetView ewv = widget.createWidgetView(values);
        widget.getWidgetViews().add(ewv);
        return ewv;
        
    }
    
    private EditorWidget generateStringWidget(String variableName, int minOccurs, int maxOccurs, EditorWidget... children){
        
        EditorWidget widget = new StringWidget();
        widget.setVariableName(variableName);
        widget.setMinOccurs(minOccurs);
        widget.setMaxOccurs(maxOccurs);
        widget.setAttributeClass(StringAttribute.class);
        
        List<EditorWidget> childWidgets = new ArrayList<>();
        for( EditorWidget child : children ){
            childWidgets.add(child);
        }
        widget.setChildren(childWidgets);
        
        return widget;
    }

}
