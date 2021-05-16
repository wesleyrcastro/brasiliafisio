package br.com.brasiliaFisio.cursos.mb;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOUsuario;
import br.com.brasiliaFisio.cursos.modelo.LoginBloqueado;
import br.com.brasiliaFisio.cursos.modelo.Usuario;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Cargo;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.TransformaStringSHA512;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(UsuarioBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Usuario> dao;
	@Inject
	private DAOUsuario daoUsuario;
	@Inject
	private DAO<LoginBloqueado> daoLoginBloqueado;

	private Usuario usuario = new Usuario();
	private LoginBloqueado loginBloqueado = new LoginBloqueado();

	private List<Usuario> usuarios;
	private List<LoginBloqueado> loginsBloqueados;

	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Informe uma senha valida!")
	@Length(min = 8, max = 20, message = "A senha deve ter entre {min} e {max} caracteres")
	private String senha;
	@SafeHtml(whitelistType = WhiteListType.NONE, message = "Informe uma confirmação de senha valida!")
	@Length(min = 8, max = 20, message = "A confirmação da senha deve ter entre {min} e {max} caracteres")
	private String confirmacaoSenha;
	private String usuarioExiste;

	@Transactional
	public String grava() {
		if (senha.equals(confirmacaoSenha)) {
			senha = TransformaStringSHA512.sha512(senha);
			usuario.setSenha(senha);

			this.dao.adiciona(usuario);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O USUARIO: " + usuario.getNome());
			limpaFormulario();
			listaTodos();

			FacesUtil.addInfoMessage("Usuário gravado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
		} else {
			FacesUtil.addErrorMessage("As senhas não conferem!");

			return "manterUsuario";
		}

		return "manterUsuario?faces-redirect=true";
	}

	@Transactional
	public String remove(Integer id) {
		Usuario usuarioParaRemover = dao.buscaPorId(id);
		dao.remove(usuarioParaRemover);
		listaTodos();

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", EXCLUIU O USUARIO: " + usuario.getNome());
		FacesUtil.addInfoMessage("Usuário excluido com sucesso!");

		return "pesquisaUsuario";
	}

	@Transactional
	public String removeLoginBloqueado(Integer id) {
		LoginBloqueado loginBloqueadoParaRemover = daoLoginBloqueado.buscaPorId(id);
		this.daoLoginBloqueado.remove(loginBloqueadoParaRemover);
		this.loginsBloqueados = daoLoginBloqueado.listaTodos();

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", DESBLOQUEOU O USUARIO: " + loginBloqueadoParaRemover.getUsuario());
		FacesUtil.addInfoMessage("Usuário desbloqueado com sucesso!");
		FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

		return "pesquisaUsuario?faces-redirect=true";
	}

	public void pesquisaUsuario() {
		this.usuarios = daoUsuario.buscaPorNome(usuarioExiste);
	}

	public String novo() {
		this.usuario = new Usuario();
		return "manterUsuario?faces-redirect=true";
	}

	private void listaTodos() {
		this.usuarios = dao.listaTodos();
	}

	private void limpaFormulario() {
		this.usuario = new Usuario();
		senha = null;
		confirmacaoSenha = null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LoginBloqueado getLoginBloqueado() {
		return loginBloqueado;
	}

	public void setLoginBloqueado(LoginBloqueado loginBloqueado) {
		this.loginBloqueado = loginBloqueado;
	}

	public List<LoginBloqueado> getLoginsBloqueados() {
		if (this.loginsBloqueados == null || this.loginsBloqueados.isEmpty()) {
			this.loginsBloqueados = daoLoginBloqueado.listaTodos();
		}
		return loginsBloqueados;
	}

	public List<Usuario> getUsuarios() {
		if (this.usuarios == null || this.usuarios.isEmpty()) {
			this.usuarios = dao.listaTodos();
		}
		return usuarios;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public String getUsuarioExiste() {
		return usuarioExiste;
	}

	public void setUsuarioExiste(String usuarioExiste) {
		this.usuarioExiste = usuarioExiste;
	}

	public Cargo[] getCargos() {
		return Cargo.values();
	}

}
