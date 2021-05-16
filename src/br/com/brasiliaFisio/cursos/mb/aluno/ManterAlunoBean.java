package br.com.brasiliaFisio.cursos.mb.aluno;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.mb.turma.ManterTurmaBean;
import br.com.brasiliaFisio.cursos.modelo.Aluno;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Estado;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Sexo;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.TransformaStringSHA512;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class ManterAlunoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ManterTurmaBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Aluno> dao;

	private Aluno aluno;

	@PostConstruct
	public void inicializar() {
		aluno = new Aluno();
	}

	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Informe uma senha valida!")
	private String senhaAluno;
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Informe uma confirmação de senha valida!")
	@Length(min = 8, max = 20, message = "A senha deve ter entre {min} e {max} caracteres!")
	private String confirmacaoSenha;

	@Transactional
	public void grava() {
		if (aluno.getId() == 0) {
			aluno.setId(null);
		}
		if (senhaAluno.equals(confirmacaoSenha) && senhaAluno != null) {
			aluno.setSenha(TransformaStringSHA512.sha512(senhaAluno));

			dao.adiciona(aluno);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O ALUNO: " + aluno.getNome()
					+ ", " + aluno.getCpf());
			limpaFormulario();

			FacesUtil.addInfoMessage("Aluno gravado com sucesso!");
		} else {
			FacesUtil.addErrorMessage("As senhas não conferem!");

		}

	}

	@Transactional
	public String altera() {
		if (senhaAluno.equals(confirmacaoSenha) && senhaAluno != null) {
			aluno.setSenha(TransformaStringSHA512.sha512(senhaAluno));

			dao.altera(aluno);
			senhaAluno = null;
			confirmacaoSenha = null;

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ATUALIZOU O ALUNO: " + aluno.getNome()
					+ ", " + aluno.getCpf());
			FacesUtil.addInfoMessage("Aluno atualizado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
		} else {
			FacesUtil.addErrorMessage("As senhas não conferem!");

			return "";
		}

		return "pesquisaAluno?faces-redirect=true";
	}

	private void limpaFormulario() {
		this.aluno = new Aluno();
		senhaAluno = null;
		confirmacaoSenha = null;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public String getSenhaAluno() {
		return senhaAluno;
	}

	public void setSenhaAluno(String senhaAluno) {
		this.senhaAluno = senhaAluno;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public Sexo[] getSexos() {
		return Sexo.values();
	}

}
