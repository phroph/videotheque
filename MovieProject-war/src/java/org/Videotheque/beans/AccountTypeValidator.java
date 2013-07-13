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
@FacesValidator("org.Videotheque.AccountTypeValidator")
public class AccountTypeValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        UIInput ccn = (UIInput) uic.getAttributes().get("ccn");
        String type = o != null ? o.toString() : null;
        String num = ccn.getSubmittedValue().toString();
        if(type != null && !type.isEmpty() && !type.split(" ")[0].equalsIgnoreCase("free") && (num == null || num.isEmpty())) {
            ccn.setValid(false);
            throw new ValidatorException(new FacesMessage("You must enter a CCN if you select a new Account Type."));
        }
    }
}
