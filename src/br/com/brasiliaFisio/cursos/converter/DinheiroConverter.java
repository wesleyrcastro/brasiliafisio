package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Dinheiro;

@RequestScoped
@FacesConverter(forClass = Dinheiro.class)
public class DinheiroConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Dinheiro dinheiro = em.find(Dinheiro.class, id);
		return dinheiro;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Dinheiro dinheiro = (Dinheiro) value;
		if (dinheiro == null || dinheiro.getId() == null) {
			return null;
		}
		return String.valueOf(dinheiro.getId());
	}

}
