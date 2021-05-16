package br.com.brasiliaFisio.cursos.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.faces.validation.InputField;

@FacesValidator("loginjaexistente")
public class UsuarioJaExistenteValidator implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@InputField
	private String login;

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
		Query q = manager.createQuery(" select count(u) from Usuario u where u.login like :pLogin ");
		q.setParameter("pLogin", login);
		Long count = (Long) q.getSingleResult();

		if (count != 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login já Existente", null));
		}

	}

}
