package br.com.brasiliaFisio.cursos.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoCheque;
import br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoDinheiro;

public class DAOGrafico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public List<ValorPorCursoCheque> relatorioValorPorCursoCheque() {
		String query = "select new br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoCheque(SUM(c.valor), c.curso) from Cheque c GROUP BY c.curso";
		return em.createQuery(query, ValorPorCursoCheque.class).getResultList();
	}

	public List<ValorPorCursoDinheiro> relatorioValorPorCursoDinheiro() {
		String query = "select new br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoDinheiro(SUM(d.valor), d.curso) from Dinheiro d GROUP BY d.curso";
		return em.createQuery(query, ValorPorCursoDinheiro.class).getResultList();
	}
}
