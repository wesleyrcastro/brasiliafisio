package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Inscricao;

@RequestScoped
@FacesConverter(forClass = Inscricao.class)
public class InscricaoConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Inscricao inscricao = em.find(Inscricao.class, id);
		return inscricao;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Inscricao inscricao = (Inscricao) value;
		if (inscricao == null || inscricao.getId() == null) {
			return null;
		}
		return String.valueOf(inscricao.getId());
	}

}
