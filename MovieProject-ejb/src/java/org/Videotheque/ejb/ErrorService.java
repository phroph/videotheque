/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.ejb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;

/**
 *
 * @author Phillip
 */
@Named
@Stateless
@LocalBean
public class ErrorService {

    // Add business logic below. (Right-click in editor and choose
    public String getAbsoluteRoot() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
    }
    public String getStackTrace() {
        // Get the current JSF context
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestMap = context.getExternalContext().getRequestMap();
 
        // Fetch the exception
        Throwable ex = (Throwable) requestMap.get("javax.servlet.error.exception");
 
        // Create a writer for keeping the stacktrace of the exception
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
 
        // Fill the stack trace into the write
        fillStackTrace(ex, pw);
 
        return writer.toString();
    }
    private void fillStackTrace(Throwable ex, PrintWriter pw) {
        if (null == ex) {
            return;
        }
 
        ex.printStackTrace(pw);
 
        // The first time fillStackTrace is called it will always
       //  be a ServletException
        if (ex instanceof ServletException) {
            Throwable cause = ((ServletException) ex).getRootCause();
            if (null != cause) {
                pw.println("Root Cause:");
                fillStackTrace(cause, pw);
            }
            } else {
            // Embedded cause inside the ServletException
            Throwable cause = ex.getCause();
 
            if (null != cause) {
                pw.println("Cause:");
                fillStackTrace(cause, pw);
            }
        }
    }
}
