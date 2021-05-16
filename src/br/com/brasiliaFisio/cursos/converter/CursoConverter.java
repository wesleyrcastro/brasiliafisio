package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Curso;

@RequestScoped
@FacesConverter(forClass = Curso.class)
public class CursoConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Curso curso = em.find(Curso.class, id);
		return curso;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Curso curso = (Curso) value;
		if (curso == null || curso.getId() == null) {
			return null;
		}
		return String.valueOf(curso.getId());
	}
}
