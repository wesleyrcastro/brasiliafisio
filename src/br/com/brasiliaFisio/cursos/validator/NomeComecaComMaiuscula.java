package br.com.brasiliaFisio.cursos.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "comecaComMaiuscula")
public class NomeComecaComMaiuscula implements Validator {

	@Override
	public void validate(FacesContext fc, UIComponent component, Object value) throws ValidatorException {

		String valor = value.toString();
		if (!valor.matches("[A-Z].*")) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Nome deverá começar com maiúscula, na sequência minúscula.", null));
		}

	}

}
