package no.met.metadataeditor;

import static no.met.metadataeditor.TestHelpers.fileAsString;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.met.metadataeditor.dataTypes.EditorVariable;
import no.met.metadataeditor.dataTypes.EditorVariableContent;
import no.met.metadataeditor.dataTypes.attributes.DataAttribute;
import no.met.metadataeditor.dataTypes.attributes.ListElementAttribute;
import no.met.metadataeditor.dataTypes.attributes.StringAttribute;
import no.met.metadataeditor.widget.EditorWidget;

import org.junit.Test;

public class EditorPageTest {

    @Test
    public void testGetContentSingleWidgetSingleValue(){
        
        String configString = fileAsString("/editorPageTest/noChildren.xml");
        EditorConfiguration config = EditorConfigurationFactory.unmarshallConfiguration(configString);
        
        Map<String,EditorVariable> varMap = TestHelpers.getVariables("/editorPageTest/noChildrenTemplate.xml");
        config.configure(varMap);
        
        EditorPage page = config.getPage("MMD");
        
        StringAttribute sa = new StringAttribute();
        sa.addAttribute("str", "dummy");
        
        List<EditorVariableContent> evcl = createEditorVariableContent(sa);
        Map<String,List<EditorVariableContent>> contentMap = new HashMap<>();
        contentMap.put("test", evcl);
        
        page.generateEditorWidgetViews(contentMap);
        
        List<EditorWidget> widgets = page.getWidgets();

        // check that the widget view have been correctly populated
        List<EditorWidgetView> ews = widgets.get(0).getWidgetViews();
        assertEquals(1, ews.size());
        assertEquals("dummy", ews.get(0).valuesAsAttriubte().getAttribute("str"));
        
        // check that we get correct values out
        Map<String,List<EditorVariableContent>> foundContent = page.getContent();
        assertEquals(contentMap, foundContent);
        
        // make a change to the values
        Map<String,String> newValues = new HashMap<>();
        newValues.put("str", "my new value");
        ews.get(0).setValues(newValues);
        
        contentMap.get("test").get(0).getAttrs().addAttribute("str", "my new value");

        // check that the new value is now found
        foundContent = page.getContent();
        assertEquals(contentMap, foundContent);
        
    }
    
    @Test
    public void testGetContentSingleWidgetMultiValue(){
        
        String configString = fileAsString("/editorPageTest/noChildren.xml");
        EditorConfiguration config = EditorConfigurationFactory.unmarshallConfiguration(configString);
        
        Map<String,EditorVariable> varMap = TestHelpers.getVariables("/editorPageTest/noChildrenTemplate.xml");
        config.configure(varMap);
        
        EditorPage page = config.getPage("MMD");
        
        StringAttribute sa1 = new StringAttribute();
        sa1.addAttribute("str", "dummy1");
        StringAttribute sa2 = new StringAttribute();
        sa2.addAttribute("str", "dummy2");      
        
        List<EditorVariableContent> evcl = createEditorVariableContent(sa1, sa2);
        Map<String,List<EditorVariableContent>> contentMap = new HashMap<>();
        contentMap.put("test", evcl);
        
        page.generateEditorWidgetViews(contentMap);
        
        List<EditorWidget> widgets = page.getWidgets();
        
        List<EditorWidgetView> ewv = widgets.get(0).getWidgetViews();
        assertEquals(2, ewv.size());
        assertEquals("dummy1", ewv.get(0).valuesAsAttriubte().getAttribute("str"));
        assertEquals("dummy2", ewv.get(1).valuesAsAttriubte().getAttribute("str"));
        
        // check that we get correct values out
        Map<String,List<EditorVariableContent>> foundContent = page.getContent();
        assertEquals(contentMap, foundContent);
        
        // make a change to the values
        Map<String,String> newValues = new HashMap<>();
        newValues.put("str", "my new value");
        ewv.get(1).setValues(newValues);
        
        contentMap.get("test").get(1).getAttrs().addAttribute("str", "my new value");

        // check that the new value is now found
        foundContent = page.getContent();
        assertEquals(contentMap, foundContent);        
        
    }    

    @Test
    public void testGetContentMultiWidgetMultiValue(){
        
        String configString = fileAsString("/editorPageTest/childrenConfig.xml");
        EditorConfiguration config = EditorConfigurationFactory.unmarshallConfiguration(configString);
        
        Map<String,EditorVariable> varMap = TestHelpers.getVariables("/editorPageTest/childrenTemplate.xml");
        config.configure(varMap);
        
        EditorPage page = config.getPage("MMD");
        
        ListElementAttribute parentSa = new ListElementAttribute();
        parentSa.addAttribute("listElement", "parent");
        StringAttribute child1Sa = new StringAttribute();
        child1Sa.addAttribute("str", "child1");
        StringAttribute child2Sa = new StringAttribute();
        child2Sa.addAttribute("str", "child2");

        
        List<EditorVariableContent> evcl = createEditorVariableContent(parentSa);
        addChildVariableContent(evcl.get(0), "child1", child1Sa);
        addChildVariableContent(evcl.get(0), "child2", child2Sa);
        
        Map<String,List<EditorVariableContent>> contentMap = new HashMap<>();
        contentMap.put("keywords", evcl);
        
        page.generateEditorWidgetViews(contentMap);
        
        List<EditorWidget> widgets = page.getWidgets();
        
        List<EditorWidgetView> ews = widgets.get(0).getWidgetViews();
        assertEquals(1, ews.size());
        assertEquals("parent", ews.get(0).valuesAsAttriubte().getAttribute("listElement"));
        
        List<EditorWidget> childWidgets = ews.get(0).getChildren();
        assertEquals(2, childWidgets.size());
        
        assertEquals(2, childWidgets.get(0).getWidgetViews().size());
        assertEquals("child1", childWidgets.get(0).getWidgetViews().get(0).valuesAsAttriubte().getAttribute("str"));
        
        // auto generated widget view due to minOccurs
        assertEquals("", childWidgets.get(0).getWidgetViews().get(1).valuesAsAttriubte().getAttribute("str"));
        
        assertEquals(1, childWidgets.get(1).getWidgetViews().size());
        assertEquals("child2", childWidgets.get(1).getWidgetViews().get(0).valuesAsAttriubte().getAttribute("str"));

        // check that we get correct values out, but need to add the auto-generated value first.
        StringAttribute autoGenSa = new StringAttribute();
        autoGenSa.addAttribute("str", "");        
        addChildVariableContent(evcl.get(0), "child1", child1Sa, autoGenSa);
        Map<String,List<EditorVariableContent>> foundContent = page.getContent();
        assertEquals(contentMap, foundContent);
        
        // make a change to the values
        Map<String,String> newValues = new HashMap<>();
        newValues.put("str", "my new value");
        childWidgets.get(0).getWidgetViews().get(0).setValues(newValues);
        
        contentMap.get("keywords").get(0).getChildren().get("child1").get(0).getAttrs().addAttribute("str", "my new value");

        // check that the new value is now found
        foundContent = page.getContent();
        assertEquals(contentMap, foundContent);                
        
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
    
}
