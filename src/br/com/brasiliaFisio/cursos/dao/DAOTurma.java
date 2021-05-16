package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

import br.com.brasiliaFisio.cursos.filter.TurmaFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Curso;
import br.com.brasiliaFisio.cursos.modelo.Turma;

public class DAOTurma implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Integer> anoTurma() {
		Query query = em.createQuery("SELECT DISTINCT YEAR(t.dataInicio) FROM Turma t", Integer.class);
		List<Integer> anos = query.getResultList();

		return anos;
	}

	public boolean existeTurmasAssociadoAoAluno(Aluno aluno) {
		Query query = this.em.createQuery("SELECT t FROM Turma t WHERE t.aluno = :pAluno");
		query.setParameter("pAluno", aluno);

		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	public boolean existeTurmasAssociadoAoCurso(Curso curso) {
		Query query = this.em.createQuery("SELECT t FROM Turma t WHERE t.curso = :pCurso");
		query.setParameter("pCurso", curso);

		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	public boolean alunoJaFoiCadastradoNaTurmaParaEsseCursoNessaData(Turma turma) {
		Query query = this.em
				.createQuery("SELECT t FROM Turma t WHERE t.aluno = :pAluno AND t.curso = :pCurso AND t.dataInicio = :pDataInicio");
		query.setParameter("pAluno", turma.getAluno());
		query.setParameter("pCurso", turma.getCurso());
		query.setParameter("pDataInicio", turma.getDataInicio());

		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	@SuppressWarnings("unchecked")
	public List<Turma> filtrados(TurmaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		String propOrder = filtro.getPropriedadeOrdenacao();
		if ("dataInicio.time".equals(propOrder)) {
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

	private Criteria criarCriteriaParaFiltro(TurmaFilter filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Turma.class).setFetchMode("aluno", FetchMode.JOIN)
				.setFetchMode("curso", FetchMode.JOIN).createAlias("aluno", "aluno").createAlias("curso", "curso");

		if (filtro.getAluno() != null) {
			criteria.add(Restrictions.eq("aluno.nome", filtro.getAluno().getNome()));
		}

		if (filtro.getCurso() != null) {
			criteria.add(Restrictions.eq("curso.nome", filtro.getCurso().getNome()));
		}

		if (filtro.getMes() != null) {
			criteria.add(Restrictions.sqlRestriction("MONTH(DATA_INICIO) = ? ", filtro.getMes().getValor(),
					new IntegerType()));
		}

		if (filtro.getAno() != null && filtro.getAno() != 0) {
			criteria.add(Restrictions.sqlRestriction("YEAR(DATA_INICIO) = ? ", filtro.getAno(), new IntegerType()));
		}

		return criteria;
	}

	public int quantidadeFiltrados(TurmaFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

}
