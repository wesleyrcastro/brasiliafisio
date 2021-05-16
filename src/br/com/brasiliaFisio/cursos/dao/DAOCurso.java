package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.brasiliaFisio.cursos.modelo.Curso;

public class DAOCurso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public List<Curso> listaTodosCurso(String curso) {

		String jpql = "SELECT c FROM Curso c WHERE UPPER (c.nome) LIKE UPPER (:pCurso) ORDER BY c.nome";
		TypedQuery<Curso> query = this.em.createQuery(jpql, Curso.class);
		query.setParameter("pCurso", "%" + curso + "%");

		return query.getResultList();
	}
}
