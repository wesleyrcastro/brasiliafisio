package br.com.brasiliaFisio.cursos.mb.turma;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOTurma;
import br.com.brasiliaFisio.cursos.modelo.Turma;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;
import br.com.brasiliaFisio.cursos.util.service.NegocioException;

@ViewController
public class ManterTurmaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ManterTurmaBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Turma> dao;
	@Inject
	private DAOTurma daoTurma;

	private Turma turma;

	@PostConstruct
	public void inicializar() {
		turma = new Turma();
	}

	@Transactional
	public String grava() {
		if (turma.getId() == null) {
			boolean isCadastrado = this.daoTurma.alunoJaFoiCadastradoNaTurmaParaEsseCursoNessaData(turma);
			if (!isCadastrado) {
				throw new NegocioException("O(A) aluno(a) " + turma.getAluno().getNome()
						+ ", já foi cadastrado no curso " + turma.getCurso().getNome() + ", que inicia na data "
						+ getDataFormatada() + "!");
			}

			dao.adiciona(turma);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O ALUNO/CURSO NA TURMA: "
					+ turma.getAluno().getNome() + ", " + turma.getCurso().getNome());
			limpaFormulario();

			FacesUtil.addInfoMessage("Aluno cadastrado na turma!");

			return "";
		} else {
			dao.altera(turma);

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ATUALIZOU O ALUNO/CURSO NA TURMA: "
					+ turma.getAluno().getNome() + ", " + turma.getCurso().getNome());
			FacesUtil.addInfoMessage("Atualizado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

			return "pesquisaTurma?faces-redirect=true";
		}
	}

	private void limpaFormulario() {
		this.turma = new Turma();
	}

	private String getDataFormatada() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(turma.getDataInicio().getTime());

	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

}
