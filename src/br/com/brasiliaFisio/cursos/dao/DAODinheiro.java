package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

import br.com.brasiliaFisio.cursos.filter.DinheiroFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Curso;
import br.com.brasiliaFisio.cursos.modelo.Dinheiro;

public class DAODinheiro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	// Retorna o Ano como um Inteiro
	@SuppressWarnings("unchecked")
	public List<Integer> anoDineiro() {
		Query query = em.createQuery("SELECT DISTINCT YEAR(d.data) FROM Dinheiro d", Integer.class);
		List<Integer> anos = query.getResultList();

		return anos;
	}

	public boolean existeDinheirosAssociadoAoAluno(Aluno aluno) {
		Query query = this.em.createQuery("SELECT d FROM Dinheiro d WHERE d.aluno = :pAluno");
		query.setParameter("pAluno", aluno);

		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	public boolean existeDinheirosAssociadoAoCurso(Curso curso) {
		Query query = this.em.createQuery("SELECT d FROM Dinheiro d WHERE d.curso = :pCurso");
		query.setParameter("pCurso", curso);

		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	@SuppressWarnings("unchecked")
	public List<Dinheiro> filtrados(DinheiroFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		String propOrder = filtro.getPropriedadeOrdenacao();
		if ("data.time".equals(propOrder)) {
			propOrder = propOrder.replace(".time", "");
			filtro.setPropriedadeOrdenacao(propOrder);
		}

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	private Criteria criarCriteriaParaFiltro(DinheiroFilter filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Dinheiro.class).createAlias("aluno", "aluno")
				.createAlias("curso", "curso");

		if (filtro.getAluno() != null) {
			criteria.add(Restrictions.eq("aluno.nome", filtro.getAluno().getNome()));
		}

		if (filtro.getCurso() != null) {
			criteria.add(Restrictions.eq("curso.nome", filtro.getCurso().getNome()));
		}

		if (filtro.getMes() != null) {
			criteria.add(Restrictions.sqlRestriction("MONTH(DATA) = ? ", filtro.getMes().getValor(), new IntegerType()));
		}

		if (filtro.getAno() != null && filtro.getAno() != 0) {
			criteria.add(Restrictions.sqlRestriction("YEAR(DATA) = ? ", filtro.getAno(), new IntegerType()));
		}

		return criteria;
	}

	public int quantidadeFiltrados(DinheiroFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

}
