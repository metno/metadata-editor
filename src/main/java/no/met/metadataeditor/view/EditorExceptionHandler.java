package no.met.metadataeditor.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.view.ViewDeclarationLanguage;

import no.met.metadataeditor.EditorException;
import no.met.metadataeditor.LogUtils;

import org.omnifaces.exceptionhandler.FullAjaxExceptionHandler;

/**
 * Extend the FullAjaxExceptionHandler with exception handling for non ajax requests.
 */
public class EditorExceptionHandler extends FullAjaxExceptionHandler {

    private static final Logger logger = Logger.getLogger(EditorExceptionHandler.class.getName());

    private ExceptionHandler wrapped;

    public EditorExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);

        this.wrapped = wrapped;
    }

    @Override
    public void handle() throws FacesException {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null || !context.getPartialViewContext().isAjaxRequest()) {
            handleNonAjaxException(FacesContext.getCurrentInstance());
        } else {
            super.handle();
        }
        
        wrapped.handle();
    }

    private void handleNonAjaxException(FacesContext context) {

        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandledExceptionQueuedEvents.hasNext()) {
            Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();

            if (exception instanceof AbortProcessingException) {
                return; // Let JSF handle it itself.
            }
            
            // go up the exception chain to the cause
            while ((exception instanceof FacesException || exception instanceof ELException )
                    && exception.getCause() != null) {
                   exception = exception.getCause();
              }

            unhandledExceptionQueuedEvents.remove();

            // put information about the exception in the log. For unhandled exceptions
            // this should not already have been done.
            LogUtils.logException(logger, exception.getMessage(), exception);
            
            String viewName;
            if (exception instanceof ViewExpiredException) {
                viewName = "/error-pages/view-expired.xhtml";
            } else if (exception instanceof EditorException ) {
                viewName = "/error-pages/editor-error.xhtml";
            } else {
                viewName = "/error-pages/server-error.xhtml";
            }

            ExternalContext externalContext = context.getExternalContext();
            Map<String, Object> requestMap = externalContext.getRequestMap();
            requestMap.put("exception", exception);
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            try {
                ViewHandler viewHandler = context.getApplication().getViewHandler();
                UIViewRoot viewRoot = viewHandler.createView(context, viewName);
                context.setViewRoot(viewRoot);
                context.getPartialViewContext().setRenderAll(true);
                ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(context, viewName);
                vdl.buildView(context, viewRoot);
                context.getApplication().publishEvent(context, PreRenderViewEvent.class, viewRoot);
                vdl.renderView(context, viewRoot);
                context.responseComplete();
            } catch (IOException e) {
                LogUtils.logException(logger, "Failed to handled exception in non ajax request.", e);
            }
            facesContext.responseComplete();
        }

        while (unhandledExceptionQueuedEvents.hasNext()) {
            // Any remaining unhandled exceptions are not interesting. First fix
            // the first.
            unhandledExceptionQueuedEvents.next();
            unhandledExceptionQueuedEvents.remove();
        }

    }
    
   
}
