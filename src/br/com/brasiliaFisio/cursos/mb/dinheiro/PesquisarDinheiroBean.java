package br.com.brasiliaFisio.cursos.mb.dinheiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.PieChartModel;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOAluno;
import br.com.brasiliaFisio.cursos.dao.DAODinheiro;
import br.com.brasiliaFisio.cursos.dao.DAOGrafico;
import br.com.brasiliaFisio.cursos.filter.DinheiroFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Dinheiro;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Mes;
import br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoDinheiro;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class PesquisarDinheiroBean extends LazyDataModel<Dinheiro> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PesquisarDinheiroBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Dinheiro> dao;
	@Inject
	private DAODinheiro daoDinheiro;
	@Inject
	private DAOAluno daoAluno;
	@Inject
	private DAOGrafico daoGrafico;

	private BigDecimal soma = new BigDecimal("0.0");
	private DinheiroFilter filtro = new DinheiroFilter();

	private List<Dinheiro> dinheiros = new ArrayList<>();
	private List<Integer> anosDinheiro = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			anosDinheiro = daoDinheiro.anoDineiro();
		}
	}

	@Override
	public List<Dinheiro> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		setRowCount(daoDinheiro.quantidadeFiltrados(filtro));

		if (getRowCount() > 0) {
			filtro.setPrimeiroRegistro(first);
			filtro.setQuantidadeRegistros(pageSize);
			filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
			filtro.setPropriedadeOrdenacao(sortField);

			dinheiros = daoDinheiro.filtrados(filtro);

			somaDinheiro();
			clean();

			return dinheiros;
		}

		return null;
	}

	private void clean() {
		DataTable tableResultado = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("frmListaDineiro:resultado");
		tableResultado.reset();
	}

	private BigDecimal somaDinheiro() {
		soma = new BigDecimal("0.0");
		for (Dinheiro dinheiro : dinheiros) {
			soma = soma.add(dinheiro.getValor());
		}
		return soma;
	}

	public PieChartModel getRelatorioValorPorCursoDinheiro() {
		List<ValorPorCursoDinheiro> list = daoGrafico.relatorioValorPorCursoDinheiro();

		PieChartModel model = new PieChartModel();
		for (ValorPorCursoDinheiro valorPorCurso : list) {
			model.set(valorPorCurso.getCurso().getNome(), valorPorCurso.getValor());
		}

		return model;
	}

	public List<Aluno> buscaNomeAluno(String nome) {
		return this.daoAluno.buscaPorNome(nome);
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDePagamentoDinheiro() {
		HashMap parametros = new HashMap();
		UtilRelatoriosLista.imprimeRelatorio("pagamentoDinheiro", parametros, dinheiros);
	}

	@Transactional
	public void remove(Integer id) {
		Dinheiro dinheiroParaRemover = dao.buscaPorId(id);
		dao.remove(dinheiroParaRemover);

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O ALUNO/CURSO DA TURMA: "
				+ dinheiroParaRemover.getAluno().getNome() + ", " + dinheiroParaRemover.getCurso().getNome());
		FacesUtil.addInfoMessage("Pagamento excluido com sucesso!");
	}

	public BigDecimal getSoma() {
		return soma;
	}

	public DinheiroFilter getFiltro() {
		return filtro;
	}

	public List<Dinheiro> getDinheiros() {
		return dinheiros;
	}

	public List<Integer> getAnosDinheiro() {
		return anosDinheiro;
	}

	public Mes[] getMeses() {
		return Mes.values();
	}

}
