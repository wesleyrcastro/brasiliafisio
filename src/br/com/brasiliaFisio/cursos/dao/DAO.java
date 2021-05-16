package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public class DAO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Class<T> classe;
	private final EntityManager em;

	public DAO(Class<T> classe, EntityManager em) {
		this.classe = classe;
		this.em = em;
	}

	public void adiciona(T t) {
		this.em.persist(t);
	}

	public void altera(T t) {
		em.merge(t);
	}

	public void remove(T t) {
		this.em.remove(em.merge(t));
	}

	public T buscaPorId(Integer id) {
		T instancia = em.find(classe, id);
		return instancia;
	}

	public T busca(Integer id) {

		return em.getReference(classe, id);
	}

	public List<T> listaTodos() {
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));
		List<T> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	public int contaTodos() {
		long result = (Long) em.createQuery("select count(n) from " + classe.getName() + " n").getSingleResult();
		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults) {
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));
		List<T> lista = em.createQuery(query).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
		return lista;
	}

}
