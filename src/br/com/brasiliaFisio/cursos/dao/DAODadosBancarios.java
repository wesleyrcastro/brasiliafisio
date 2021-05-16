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

import br.com.brasiliaFisio.cursos.filter.DadosBacariosFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Cheque;
import br.com.brasiliaFisio.cursos.modelo.Curso;

import com.ocpsoft.pretty.faces.util.StringUtils;

public class DAODadosBancarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	// Retorna o Ano Entrega como um Inteiro
	@SuppressWarnings("unchecked")
	public List<Integer> anoEntregaCheque() {
		Query query = em.createQuery("SELECT DISTINCT YEAR(c.dataEntrega) FROM Cheque c", Integer.class);
		List<Integer> anos = query.getResultList();

		return anos;
	}

	// Retorna o Ano Deposito como um Inteiro
	@SuppressWarnings("unchecked")
	public List<Integer> anoDepositoCheque() {
		Query query = em.createQuery("SELECT DISTINCT YEAR(c.dataDeposito) FROM Cheque c", Integer.class);
		List<Integer> anos = query.getResultList();

		return anos;
	}

	public boolean existeChequesAssociadoAoAluno(Aluno aluno) {
		Query query = this.em.createQuery("SELECT c FROM Cheque c WHERE c.aluno = :pAluno");
		query.setParameter("pAluno", aluno);
		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	public boolean existeChequesAssociadoAoCurso(Curso curso) {
		Query query = this.em.createQuery("SELECT c FROM Cheque c WHERE c.curso = :pCurso");
		query.setParameter("pCurso", curso);
		boolean encontrado = query.getResultList().isEmpty();

		return encontrado;
	}

	// Filtro Dinamico
	@SuppressWarnings("unchecked")
	public List<Cheque> filtrados(DadosBacariosFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		String propOrder = filtro.getPropriedadeOrdenacao();
		if ("dataEntrega.time".equals(propOrder)) {
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

	private Criteria criarCriteriaParaFiltro(DadosBacariosFilter filtro) {
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cheque.class).createAlias("aluno", "aluno").createAlias("curso", "curso");

		if (filtro.getAluno() != null) {
			criteria.add(Restrictions.eq("aluno.nome", filtro.getAluno().getNome()));
		}

		if (StringUtils.isNotBlank(filtro.getNumCheque())) {
			criteria.add(Restrictions.eq("numCheque", filtro.getNumCheque()));
		}

		if (filtro.getCurso() != null) {
			criteria.add(Restrictions.eq("curso.nome", filtro.getCurso().getNome()));
		}

		if (filtro.getMesEntrega() != null) {
			criteria.add(Restrictions.sqlRestriction("MONTH(DATA_ENTREGA) = ? ", filtro.getMesEntrega().getValor(),
					new IntegerType()));
		}

		if (filtro.getAnoEntrega() != null && filtro.getAnoEntrega() != 0) {
			criteria.add(Restrictions.sqlRestriction("YEAR(DATA_ENTREGA) = ? ", filtro.getAnoEntrega(),
					new IntegerType()));
		}

		if (filtro.getMesDeposito() != null) {
			criteria.add(Restrictions.sqlRestriction("MONTH(DATA_DEPOSITO) = ? ", filtro.getMesDeposito().getValor(),
					new IntegerType()));
		}

		if (filtro.getAnoDeposito() != null && filtro.getAnoDeposito() != 0) {
			criteria.add(Restrictions.sqlRestriction("YEAR(DATA_DEPOSITO) = ? ", filtro.getAnoDeposito(),
					new IntegerType()));
		}

		return criteria;
	}

	public int quantidadeFiltrados(DadosBacariosFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue();
	}

}