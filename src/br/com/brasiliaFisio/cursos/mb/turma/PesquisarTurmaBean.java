package br.com.brasiliaFisio.cursos.mb.turma;

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
import br.com.brasiliaFisio.cursos.dao.DAOTurma;
import br.com.brasiliaFisio.cursos.filter.TurmaFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.Turma;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Mes;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class PesquisarTurmaBean extends LazyDataModel<Turma> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PesquisarTurmaBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Turma> dao;
	@Inject
	private DAOAluno daoAluno;
	@Inject
	private DAOTurma daoTurma;

	private TurmaFilter filtro = new TurmaFilter();

	private List<Turma> turmas = new ArrayList<>();
	private List<Integer> anosTurmas = new ArrayList<>();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			anosTurmas = daoTurma.anoTurma();
		}
	}

	@Override
	public List<Turma> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		setRowCount(daoTurma.quantidadeFiltrados(filtro));

		if (getRowCount() > 0) {
			filtro.setPrimeiroRegistro(first);
			filtro.setQuantidadeRegistros(pageSize);
			filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
			filtro.setPropriedadeOrdenacao(sortField);

			turmas = daoTurma.filtrados(filtro);

			clean();

			return turmas;
		}

		return null;
	}

	private void clean() {
		DataTable tableResultado = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("frmLista:tbl");
		tableResultado.reset();

	}

	public List<Aluno> buscaNomeAluno(String nome) {
		return this.daoAluno.buscaPorNome(nome);
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDaTurma() {
		HashMap parametros = new HashMap();
		UtilRelatoriosLista.imprimeRelatorio("turma", parametros, turmas);
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

	@Transactional
	public void remove(Integer id) {
		Turma turmaParaRemover = dao.buscaPorId(id);
		dao.remove(turmaParaRemover);

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O ALUNO/CURSO DA TURMA: "
				+ turmaParaRemover.getAluno().getNome() + ", " + turmaParaRemover.getCurso().getNome());
		FacesUtil.addInfoMessage("Aluno excluido da turma!");

	}

	public TurmaFilter getFiltro() {
		return filtro;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public List<Integer> getAnosTurmas() {
		return anosTurmas;
	}

	public Mes[] getMeses() {
		return Mes.values();
	}

}
