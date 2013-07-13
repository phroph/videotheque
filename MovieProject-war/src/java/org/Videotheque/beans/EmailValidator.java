/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.Videotheque.beans;

import java.beans.*;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.Videotheque.ejb.UserService;

/**
 *
 * @author Phillip
 */
@ManagedBean
@RequestScoped
public class EmailValidator implements Validator {

    @EJB
    UserService userService;
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String email = o.toString();
        if(email == null || email.isEmpty()) {
            return;
        }
        if(!userService.checkEmail(email)) {
            throw new ValidatorException(new FacesMessage("Email is already associated with an account."));
        }
    }
    
    
}
