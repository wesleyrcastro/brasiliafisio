package br.com.brasiliaFisio.cursos.mb.dinheiro;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.modelo.Dinheiro;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class ManterDinheiroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ManterDinheiroBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;

	@Inject
	private DAO<Dinheiro> dao;

	private Dinheiro dinheiro;

	@PostConstruct
	public void inicializar() {
		dinheiro = new Dinheiro();
	}

	@Transactional
	public String grava() {

		if (dinheiro.getId() == null) {
			dao.adiciona(dinheiro);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O DINHEIRO: "
					+ dinheiro.getAluno().getNome() + ", " + dinheiro.getCurso().getNome());
			limpaFormulario();

			FacesUtil.addInfoMessage("Pagamento gravado com sucesso!");
			return "";
		} else {
			dao.altera(dinheiro);

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ATUALIZOU O DINHEIRO: "
					+ dinheiro.getAluno().getNome() + ", " + dinheiro.getCurso().getNome());
			FacesUtil.addInfoMessage("Pagamento atualizado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

			return "pesquisaDinheiro?faces-redirect=true";
		}
	}

	private void limpaFormulario() {
		dinheiro = new Dinheiro();
	}

	public Dinheiro getDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(Dinheiro dinheiro) {
		this.dinheiro = dinheiro;
	}

}
