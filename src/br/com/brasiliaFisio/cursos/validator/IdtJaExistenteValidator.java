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

@FacesValidator("idtjaexistente")
public class IdtJaExistenteValidator implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	@InputField
	private String identidade;

	@Inject
	@InputField
	private Integer idt;

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
		Query q = em.createQuery(" select count(a) from Aluno a where a.identidade like :pIdentidade "
				+ (idt != null ? " and a.id != " + idt : ""));
		q.setParameter("pIdentidade", identidade);
		Long count = (Long) q.getSingleResult();

		if (count != 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "RG já existente", null));
		}

	}

}
