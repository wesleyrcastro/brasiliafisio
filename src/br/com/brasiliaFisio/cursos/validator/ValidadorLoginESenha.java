package br.com.brasiliaFisio.cursos.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import org.jboss.seam.faces.validation.InputField;

@FacesValidator("loginesenha")
public class ValidadorLoginESenha implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@InputField
	private String login;

	@Inject
	@InputField
	private String senha;

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {

		if (login != null && senha != null && login.equals(senha)) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Login e Senha não podem ser iguais", null));
		}

	}

}
