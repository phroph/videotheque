/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.beans;

import java.beans.*;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Phillip
 */
@FacesValidator("org.Videotheque.PasswordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        UIInput confirmPassword = (UIInput) uic.getAttributes().get("confirmPassword");
        String password = o.toString();
        String confPassword = confirmPassword.getSubmittedValue().toString();
        if((password == null || password.isEmpty()) && (confPassword == null || confPassword.isEmpty())) {
            return;
        }
        if(!password.equals(confPassword)) {
            confirmPassword.setValid(false);
            throw new ValidatorException(new FacesMessage("Passwords must match."));
        }
    }
    
   
}
