package br.com.brasiliaFisio.cursos.mb.dadosBancarios;

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
import br.com.brasiliaFisio.cursos.dao.DAODadosBancarios;
import br.com.brasiliaFisio.cursos.dao.DAOGrafico;
import br.com.brasiliaFisio.cursos.filter.DadosBacariosFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Cheque;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Mes;
import br.com.brasiliaFisio.cursos.modelo.vo.ValorPorCursoCheque;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class PesquisarDadosBancariosBean extends LazyDataModel<Cheque> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PesquisarDadosBancariosBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Cheque> dao;
	@Inject
	private DAODadosBancarios daoDadosBancarios;
	@Inject
	private DAOAluno daoAluno;
	@Inject
	private DAOGrafico daoGrafico;

	private BigDecimal soma = new BigDecimal("0.0");
	private DadosBacariosFilter filtro = new DadosBacariosFilter();

	private List<Cheque> cheques = new ArrayList<>();
	private List<Integer> anoEntregaCheques = new ArrayList<>();
	private List<Integer> anoDepositoCheques = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			anoEntregaCheques = daoDadosBancarios.anoEntregaCheque();
			anoDepositoCheques = daoDadosBancarios.anoDepositoCheque();
		}
	}

	@Override
	public List<Cheque> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		setRowCount(daoDadosBancarios.quantidadeFiltrados(filtro));

		if (getRowCount() > 0) {
			filtro.setPrimeiroRegistro(first);
			filtro.setQuantidadeRegistros(pageSize);
			filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
			filtro.setPropriedadeOrdenacao(sortField);

			cheques = daoDadosBancarios.filtrados(filtro);

			somaCheque();
			clean();

			return cheques;
		}

		return null;
	}

	private void clean() {
		DataTable tableResultado = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("frmListaCheque:resultado");
		tableResultado.reset();
	}

	private BigDecimal somaCheque() {
		soma = new BigDecimal("0.0");
		for (Cheque cheque : cheques) {
			soma = soma.add(cheque.getValor());
		}
		return soma;
	}

	public PieChartModel getRelatorioValorPorCursoCheque() {
		List<ValorPorCursoCheque> list = daoGrafico.relatorioValorPorCursoCheque();

		PieChartModel model = new PieChartModel();
		for (ValorPorCursoCheque valorPorCurso : list) {
			model.set(valorPorCurso.getCurso().getNome(), valorPorCurso.getValor());
		}

		return model;
	}

	public List<Aluno> buscaNomeAluno(String nome) {
		return this.daoAluno.buscaPorNome(nome);
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDePagamentoCheque() {
		HashMap parametros = new HashMap();
		UtilRelatoriosLista.imprimeRelatorio("pagamentoCheque", parametros, cheques);
	}

	@Transactional
	public void remove(Integer id) {
		Cheque dadosBancariosParaRemover = dao.buscaPorId(id);
		dao.remove(dadosBancariosParaRemover);

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O CHEQUE: "
				+ dadosBancariosParaRemover.getAluno().getNome() + ", "
				+ dadosBancariosParaRemover.getCurso().getNome());
		FacesUtil.addInfoMessage("Pagamento excluido com sucesso!");
	}

	public BigDecimal getSoma() {
		return soma;
	}

	public DadosBacariosFilter getFiltro() {
		return filtro;
	}

	public List<Cheque> getCheques() {
		return cheques;
	}

	public List<Integer> getAnoEntregaCheques() {
		return anoEntregaCheques;
	}

	public List<Integer> getAnoDepositoCheques() {
		return anoDepositoCheques;
	}

	public Mes[] getMesesEntrega() {
		return Mes.values();
	}

	public Mes[] getMesesDeposito() {
		return Mes.values();
	}

}
