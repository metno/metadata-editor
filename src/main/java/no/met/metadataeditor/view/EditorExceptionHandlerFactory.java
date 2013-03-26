package no.met.metadataeditor.view;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.omnifaces.exceptionhandler.FullAjaxExceptionHandlerFactory;

public class EditorExceptionHandlerFactory extends FullAjaxExceptionHandlerFactory {

    private final ExceptionHandlerFactory wrapped;

    public EditorExceptionHandlerFactory(final ExceptionHandlerFactory wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new EditorExceptionHandler(wrapped.getExceptionHandler());
    }

}
