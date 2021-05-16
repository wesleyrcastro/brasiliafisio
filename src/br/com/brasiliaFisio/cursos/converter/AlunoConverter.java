package br.com.brasiliaFisio.cursos.converter;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.Aluno;

@RequestScoped
@FacesConverter(forClass = Aluno.class)
public class AlunoConverter implements Converter {

	@Inject
	private EntityManager em;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
		if (value == null || value.isEmpty() || "null".equals(value)) {
			return null;
		}
		Integer id = Integer.valueOf(value);
		Aluno aluno = em.find(Aluno.class, id);
		return aluno;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Aluno aluno = (Aluno) value;
		if (aluno == null || aluno.getId() == null) {
			return null;
		}
		return String.valueOf(aluno.getId());
	}

}
