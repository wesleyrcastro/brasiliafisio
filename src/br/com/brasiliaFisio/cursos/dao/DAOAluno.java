package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.brasiliaFisio.cursos.filter.AlunoFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;

public class DAOAluno implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public List<Aluno> listaTodosAlunos() {
		CriteriaBuilder builder = this.em.getCriteriaBuilder();
		CriteriaQuery<Aluno> criteria = builder.createQuery(Aluno.class);
		Root<Aluno> root = criteria.from(Aluno.class);

		criteria.orderBy(builder.asc((root.<Aluno> get("nome"))));

		return this.em.createQuery(criteria).getResultList();
	}

	public List<Aluno> buscaPorNome(String nome) {
		String jpql = "SELECT a FROM Aluno a WHERE UPPER (a.nome) LIKE UPPER (:pAluno) ORDER BY a.nome";
		TypedQuery<Aluno> query = this.em.createQuery(jpql, Aluno.class);
		query.setParameter("pAluno", "%" + nome + "%");

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> filtrados(AlunoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	private Criteria criarCriteriaParaFiltro(AlunoFilter filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Aluno.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria;
	}

	public int quantidadeFiltrados(AlunoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

}
