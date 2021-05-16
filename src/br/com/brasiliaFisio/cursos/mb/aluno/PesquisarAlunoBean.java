package br.com.brasiliaFisio.cursos.mb.aluno;

import java.io.Serializable;
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

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOAluno;
import br.com.brasiliaFisio.cursos.dao.DAODadosBancarios;
import br.com.brasiliaFisio.cursos.dao.DAODinheiro;
import br.com.brasiliaFisio.cursos.dao.DAOInscricao;
import br.com.brasiliaFisio.cursos.dao.DAOTurma;
import br.com.brasiliaFisio.cursos.filter.AlunoFilter;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;
import br.com.brasiliaFisio.cursos.util.service.NegocioException;

@ViewController
public class PesquisarAlunoBean extends LazyDataModel<Aluno> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(PesquisarAlunoBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Aluno> dao;
	@Inject
	private DAOAluno daoAluno;
	@Inject
	private DAOTurma daoTurma;
	@Inject
	private DAODadosBancarios daoCheque;
	@Inject
	private DAODinheiro daoDinheiro;
	@Inject
	private DAOInscricao daoInscricao;

	private AlunoFilter filtro = new AlunoFilter();

	private List<Aluno> alunos = new ArrayList<>();

	@Override
	public List<Aluno> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		setRowCount(daoAluno.quantidadeFiltrados(filtro));

		if (getRowCount() > 0) {
			filtro.setPrimeiroRegistro(first);
			filtro.setQuantidadeRegistros(pageSize);
			filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
			filtro.setPropriedadeOrdenacao(sortField);

			alunos = daoAluno.filtrados(filtro);
			clean();

			return alunos;
		}

		return null;
	}

	private void clean() {
		DataTable tableResultado = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent("frmLista:resultado");
		tableResultado.reset();
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDeAluno() {
		HashMap parametros = new HashMap();
		UtilRelatoriosLista.imprimeRelatorio("aluno", parametros, alunos);
	}

	@Transactional
	public void remove(Integer id) {
		Aluno alunoParaRemover = dao.buscaPorId(id);
		boolean existeTurmaComEsteAluno = this.daoTurma.existeTurmasAssociadoAoAluno(alunoParaRemover);
		boolean existeChequeComEsteAluno = this.daoCheque.existeChequesAssociadoAoAluno(alunoParaRemover);
		boolean existeDinheiroComEsteAluno = this.daoDinheiro.existeDinheirosAssociadoAoAluno(alunoParaRemover);
		boolean existeInscricaoComEsteAluno = this.daoInscricao.existeInscricoesAssociadoAoAluno(alunoParaRemover);

		if (existeTurmaComEsteAluno && existeChequeComEsteAluno && existeDinheiroComEsteAluno
				&& existeInscricaoComEsteAluno) {
			dao.remove(alunoParaRemover);

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O ALUNO: "
					+ alunoParaRemover.getNome() + ", " + alunoParaRemover.getCpf());
			FacesUtil.addInfoMessage("Aluno excluido com sucesso!");
		} else {
			throw new NegocioException("Existe registro associado a este aluno. Não pode excluir, mas pode editar.");
		}
	}

	public AlunoFilter getFiltro() {
		return filtro;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

}
