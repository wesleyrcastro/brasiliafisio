package br.com.brasiliaFisio.cursos.mb;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.SessionController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.dao.DAOLoginBloqueado;
import br.com.brasiliaFisio.cursos.dao.DAOUsuario;
import br.com.brasiliaFisio.cursos.modelo.LoginBloqueado;
import br.com.brasiliaFisio.cursos.modelo.Usuario;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@SessionController
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Log logger = LogFactory.getLog(LoginBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAOUsuario dao;
	@Inject
	private DAO<LoginBloqueado> daoBloqueado;
	@Inject
	private DAOLoginBloqueado loginBloqueadoDAO;

	private LoginBloqueado loginBloqueado = new LoginBloqueado();
	private Usuario usuario = new Usuario();

	private int tentativasErradas = 0;
	private static String[] mensagens = { "Ops..., você errou. Verifique os dados e tente novamente.",
			"Esqueceu sua senha?", "Se errar de novo, te bloqueio." };

	@Transactional
	public String efetuaLogin() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		// @TODO Buscar loginBloqueado com DAO, buscando pelo IP
		boolean isLoginBloqueado = loginBloqueadoDAO.existe(ipAddress);

		if (isLoginBloqueado) {
			FacesUtil.addWarnMessage("Usuário bloqueado. Contate o administrador.");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
			
			logger.info("USUÁRIO BLOQUEADO: " + usuario.getLogin());
			
			return "/pagina/login/index?faces-redirect=true";
		}

		logger.info("USUÁRIO TENTANDO ACESSAR A APLICAÇÃO: " + usuario.getLogin());
		Usuario loginUsuario = dao.existe(usuario);

		if (loginUsuario != null) {
			usuarioLogado.logar(loginUsuario);
			logger.info("USUÁRIO ACESSOU A APLICAÇÃO: " + usuario.getLogin());
			
			return "/pagina/paginaInicial/paginaInicial?faces-redirect=true";
		} else {
			tentativasErradas++;

			if (tentativasErradas > 3) {
				loginBloqueado.setData(new Date());
				loginBloqueado.setUsuario(usuario.getLogin());
				loginBloqueado.setIp(ipAddress);

				// @TODO Salvar loginbloqueado com DAO
				daoBloqueado.adiciona(loginBloqueado);
				tentativasErradas = 0;
			} else {
				FacesUtil.addWarnMessage(mensagens[tentativasErradas - 1]);
				FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
				
				return "/pagina/login/index?faces-redirect=true";
			}

			usuarioLogado.deslogar();
			usuario = new Usuario();
			
			FacesUtil.addErrorMessage("Login Invalido");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
			
			logger.info("USUÁRIO FOI BLOQUEADO: " + usuario.getLogin());
		}

		return "/pagina/login/index?faces-redirect=true";
	}
	
	public String logout() {
		logger.info("USUÁRIO SAIU DA APLICAÇÃO: " + usuario.getLogin());

		setUsuario(null);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/pagina/login/index?faces-redirect=true";
	}

	public boolean isLogado() {
		return usuarioLogado.isLogado();

	}

	public LoginBloqueado getLoginBloqueado() {
		return loginBloqueado;
	}

	public void setLoginBloqueado(LoginBloqueado loginBloqueado) {
		this.loginBloqueado = loginBloqueado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getTentativasErradas() {
		return tentativasErradas;
	}

	public void setTentativasErradas(int tentativasErradas) {
		this.tentativasErradas = tentativasErradas;
	}

}
