package br.com.brasiliaFisio.cursos.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOCurso;
import br.com.brasiliaFisio.cursos.dao.DAODadosBancarios;
import br.com.brasiliaFisio.cursos.dao.DAODinheiro;
import br.com.brasiliaFisio.cursos.dao.DAOInscricao;
import br.com.brasiliaFisio.cursos.dao.DAOTurma;
import br.com.brasiliaFisio.cursos.modelo.Curso;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Status;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.report.UtilRelatoriosLista;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;
import br.com.brasiliaFisio.cursos.util.service.NegocioException;

@ViewController
public class CursoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(CursoBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;

	@Inject
	private DAO<Curso> dao;
	@Inject
	private DAOCurso daoCurso;
	@Inject
	private DAOTurma daoTurma;
	@Inject
	private DAODadosBancarios daoCheque;
	@Inject
	private DAODinheiro daoDinheiro;
	@Inject
	private DAOInscricao daoInscricao;

	private Curso curso = new Curso();

	private List<Curso> cursos = new ArrayList<>();

	private String cursoExiste;

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			cursos = dao.listaTodos();
		}
	}

	@Transactional
	public String grava() {

		if (curso.getId() == 0) {
			curso.setId(null);
			dao.adiciona(curso);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O CURSO: " + curso.getNome());
			limpaFormulario();
			listaTodos();

			FacesUtil.addInfoMessage("Curso gravado com sucesso!");

			return "";
		} else {
			dao.altera(curso);

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ATUALIZOU O CURSO: " + curso.getNome());
			FacesUtil.addInfoMessage("Curso atualizado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

			return "pesquisaCurso?faces-redirect=true";
		}
	}

	@Transactional
	public void remove(Integer id) {
		Curso cursoParaRemover = dao.buscaPorId(id);
		boolean existeTurmaComEsteCurso = this.daoTurma.existeTurmasAssociadoAoCurso(cursoParaRemover);
		boolean existeChequeComEsteCurso = this.daoCheque.existeChequesAssociadoAoCurso(cursoParaRemover);
		boolean existeDinheiroComEsteCurso = this.daoDinheiro.existeDinheirosAssociadoAoCurso(cursoParaRemover);
		boolean existeInscricaoComEsteCurso = this.daoInscricao.existeInscricoesAssociadoAoCurso(cursoParaRemover);

		if (existeTurmaComEsteCurso && existeChequeComEsteCurso && existeDinheiroComEsteCurso
				&& existeInscricaoComEsteCurso) {
			dao.remove(cursoParaRemover);
			listaTodos();

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O CURSO: " + cursoParaRemover.getNome());
			FacesUtil.addInfoMessage("Curso excluido com sucesso!");
		} else {
			throw new NegocioException("Existe registro associado a este curso. Não pode excluir, mas pode editar.");
		}
	}

	public void pesquisaCurso() {
		cursos = daoCurso.listaTodosCurso(cursoExiste);
	}

	@SuppressWarnings("rawtypes")
	public void relatorioDeCurso() {
		HashMap parametros = new HashMap();

		UtilRelatoriosLista.imprimeRelatorio("curso", parametros, cursos);
	}

	public String novo() {
		this.curso = new Curso();
		return "manterCurso?faces-redirect=true";
	}

	private void listaTodos() {
		this.cursos = dao.listaTodos();
	}

	public List<Curso> getListaComboComCurso() {
		if (this.cursos == null || this.cursos.isEmpty()) {
			this.cursos = dao.listaTodos();
		}
		return cursos;
	}

	private void limpaFormulario() {
		this.curso = new Curso();
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public String getCursoExiste() {
		return cursoExiste;
	}

	public void setCursoExiste(String cursoExiste) {
		this.cursoExiste = cursoExiste;
	}

	public Status[] getStatus() {
		return Status.values();
	}

}
