package br.com.brasiliaFisio.cursos.mb.inscricao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.modelo.Inscricao;
import br.com.brasiliaFisio.cursos.modelo.enumeration.Indicacao;
import br.com.brasiliaFisio.cursos.util.email.EmailResposta;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class ManterInscricaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ManterInscricaoBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;

	@Inject
	private DAO<Inscricao> dao;

	private Inscricao inscricao;

	@PostConstruct
	public void inicializar() {
		inscricao = new Inscricao();
	}

	@Transactional
	public String alterar() {
		dao.altera(inscricao);

		logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU A INSCRIÇÃO: "
				+ inscricao.getAluno().getNome() + ", " + inscricao.getCurso().getNome());
		EmailResposta.enviaEmail(inscricao);

		return "pesquisaInscricao?faces-redirect=true";
	}

	public Inscricao getInscricao() {
		return inscricao;
	}

	public void setInscricao(Inscricao inscricao) {
		this.inscricao = inscricao;
	}

	public Indicacao[] getIndicacoes() {
		return Indicacao.values();
	}

}
