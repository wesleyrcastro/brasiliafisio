package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Cheque;

@RequestScoped
@FacesConverter(forClass = Cheque.class)
public class DadosBancariosConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Cheque cheque = em.find(Cheque.class, id);
		return cheque;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Cheque cheque = (Cheque) value;
		if (cheque == null || cheque.getId() == null) {
			return null;
		}
		return String.valueOf(cheque.getId());
	}

}
