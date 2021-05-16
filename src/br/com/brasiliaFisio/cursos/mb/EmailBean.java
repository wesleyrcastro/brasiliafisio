package br.com.brasiliaFisio.cursos.mb;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.modelo.Mensagem;
import br.com.brasiliaFisio.cursos.util.email.EmailUtils;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class EmailBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(EmailBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;

	private Mensagem mensagem = new Mensagem();

	public void enviaEmail() {
		EmailUtils.conectaEmail(mensagem);
		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ENVIOU E-MAIL: " + mensagem.getDestino()
				+ ", " + mensagem.getMensagem());
		limpaCampos();
	}

	public void limpaCampos() {
		mensagem = new Mensagem();
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

}
