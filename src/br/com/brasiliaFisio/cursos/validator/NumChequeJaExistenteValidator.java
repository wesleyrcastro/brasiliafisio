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

@FacesValidator("numchequejaexistente")
public class NumChequeJaExistenteValidator implements Validator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@Inject
	@InputField
	private String numCheque;

	@Inject
	@InputField
	private Integer pid;

	@Override
	public void validate(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
		Query q = em.createQuery("select count(c) from Cheque c where c.numCheque like :pNumCheque"
				+ (pid != null ? " and c.id != " + pid : ""));
		q.setParameter("pNumCheque", numCheque);
		Long count = (Long) q.getSingleResult();

		if (count != 0) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "N° do Cheque já existente",
					null));
		}

	}

}
