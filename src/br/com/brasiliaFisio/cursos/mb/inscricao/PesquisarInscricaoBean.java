package br.com.brasiliaFisio.cursos.mb.inscricao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOAluno;
import br.com.brasiliaFisio.cursos.dao.DAOInscricao;
import br.com.brasiliaFisio.cursos.filter.InscricaoFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Inscricao;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Indicacao;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Mes;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class PesquisarInscricaoBean extends LazyDataModel<Inscricao> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PesquisarInscricaoBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Inscricao> dao;
	@Inject
	private DAOInscricao daoInscricao;
	@Inject
	private DAOAluno daoAluno;

	private InscricaoFilter filtro = new InscricaoFilter();

	private List<Inscricao> inscricoes = new ArrayList<>();
	private List<Integer> anosIncricoes = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			anosIncricoes = daoInscricao.anoInscricao();
		}
	}

	@Override
	public List<Inscricao> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		setRowCount(daoInscricao.quantidadeFiltrados(filtro));

		if (getRowCount() > 0) {
			filtro.setPrimeiroRegistro(first);
			filtro.setQuantidadeRegistros(pageSize);
			filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
			filtro.setPropriedadeOrdenacao(sortField);

			inscricoes = daoInscricao.filtrados(filtro);

			clean();

			return inscricoes;
		}

		return null;
	}

	private void clean() {
		DataTable tableResultado = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("frmLista:tbl");
		tableResultado.reset();
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDeInscricao() {
		HashMap parametros = new HashMap();
		UtilRelatoriosLista.imprimeRelatorio("inscricao", parametros, inscricoes);

	}

	@Transactional
	public void remove(Integer id) {
		Inscricao inscricaoParaRemover = dao.buscaPorId(id);
		dao.remove(inscricaoParaRemover);

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU A INSCRIÇÃO: "
				+ inscricaoParaRemover.getAluno().getNome() + ", " + inscricaoParaRemover.getCurso().getNome());
		FacesUtil.addInfoMessage("Pré-inscrição excluido com sucesso!");
	}

	public List<Aluno> buscaNomeAluno(String nome) {
		return this.daoAluno.buscaPorNome(nome);
	}

	public void postProcessXLS(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			HSSFCell cell = header.getCell(i);

			cell.setCellStyle(cellStyle);
		}
	}

	public InscricaoFilter getFiltro() {
		return filtro;
	}

	public List<Inscricao> getInscricoes() {
		return inscricoes;
	}

	public List<Integer> getAnosIncricoes() {
		return anosIncricoes;
	}

	public Mes[] getMeses() {
		return Mes.values();
	}

	public Indicacao[] getIndicacoes() {
		return Indicacao.values();
	}

}
