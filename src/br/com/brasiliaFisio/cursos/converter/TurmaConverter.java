package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Turma;

@RequestScoped
@FacesConverter(forClass = Turma.class)
public class TurmaConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Turma turma = em.find(Turma.class, id);
		return turma;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Turma turma = (Turma) value;
		if (turma == null || turma.getId() == null) {
			return null;
		}
		return String.valueOf(turma.getId());
	}

}
