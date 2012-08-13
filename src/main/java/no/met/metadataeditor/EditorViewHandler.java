package no.met.metadataeditor;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewParameter;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewMetadata;

public class EditorViewHandler extends ViewHandlerWrapper {

    private ViewHandler wrapped;
    
    private Pattern editorUrlPattern = Pattern.compile(".*editor\\.xhtml.*");
    
    public EditorViewHandler(ViewHandler wrapped){
        this.wrapped = wrapped;
    }
    
    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }
    
    @Override
    public String getActionURL(FacesContext context, String viewId) {
        String originalActionURL = super.getActionURL(context, viewId);
        String newActionURL = addEditorViewParams(context, originalActionURL);
        return newActionURL;
    }

    
    public String addEditorViewParams(FacesContext context, String actionUrl){

        Matcher m = editorUrlPattern.matcher(actionUrl);
        if(m.matches()){

            actionUrl += "?";
            
            Collection<UIViewParameter> viewParams = ViewMetadata.getViewParameters(FacesContext.getCurrentInstance().getViewRoot());
            for (UIViewParameter viewParam : viewParams) {
                String name = viewParam.getName();
                Object value = viewParam.getValue();
                actionUrl += name + "=" + value + "&";
            }
        }
        
        return actionUrl;
    }
    
    


}
