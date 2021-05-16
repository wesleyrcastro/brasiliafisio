package br.com.brasiliaFisio.cursos.util.security;

import java.io.Serializable;

import br.com.brasiliaFisio.cursos.anotation.stereotype.SessionController;
import br.com.brasiliaFisio.cursos.modelo.Usuario;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Cargo;

@SessionController
public class UsuarioLogado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public Boolean getControleAcesso() throws Exception {
		return (usuario != null && usuario.getCargo() != null && usuario.getCargo().equals(Cargo.Gerente));
	}
	
	public void logar(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void deslogar() {
		this.usuario = null;
	}

	public boolean isLogado() {
		return usuario != null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
