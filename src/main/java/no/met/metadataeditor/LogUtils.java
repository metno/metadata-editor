package no.met.metadataeditor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {

    /**
     * Log an exception both with the stack trace for developers and with a message
     * for the people doing the operations.
     * @param logger The logger to use to log the exception.
     * @param message The message that will be logged both the developers and operations people
     * @param ex The exception that should be logged with stack trace for the developers.
     */
    public static void logException(Logger logger, String message, Throwable t){
        
        // get the stack trace in the fastest way. see
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6375302
        StackTraceElement[] trace = new Throwable().getStackTrace();
        
        // we want to report the caller as one level up in the stack. Otherwise
        // logException will be the caller for all logged exceptions.
        StackTraceElement caller = trace[1];
        
        logger.logp(Level.SEVERE, caller.getClassName(), caller.getMethodName(), message);        
        logger.logp(Level.INFO, caller.getClassName(), caller.getMethodName(), message, t);
        
    }
    
    
    
}
