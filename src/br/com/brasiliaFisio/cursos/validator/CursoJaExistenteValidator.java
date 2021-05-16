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

@FacesValidator("cursojaexistente")
public class CursoJaExistenteValidator implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	@InputField
	private String nome;

	@Inject
	@InputField
	private Integer pid;

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
		Query q = em.createQuery(" select count(c) from Curso c where c.nome like :pNome"
				+ (pid != null ? " and c.id != " + pid : ""));
		q.setParameter("pNome", nome);
		Long count = (Long) q.getSingleResult();

		if (count != 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Curso já existente!", null));
		}

	}

}
