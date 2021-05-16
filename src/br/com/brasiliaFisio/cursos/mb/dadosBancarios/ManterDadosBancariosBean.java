package br.com.brasiliaFisio.cursos.mb.dadosBancarios;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.anotation.Transactional;
import br.com.brasiliaFisio.cursos.anotation.stereotype.ViewController;
import br.com.brasiliaFisio.cursos.dao.DAO;
import br.com.brasiliaFisio.cursos.modelo.Cheque;
import br.com.brasiliaFisio.cursos.modelo.enumeration.NomeDoBanco;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.security.UsuarioLogado;

@ViewController
public class ManterDadosBancariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ManterDadosBancariosBean.class);

	@Inject
	private UsuarioLogado usuarioLogado;
	@Inject
	private DAO<Cheque> dao;

	private Cheque cheque;

	@PostConstruct
	public void inicializar() {
		cheque = new Cheque();
	}

	@Transactional
	public String grava() {

		if (cheque.getId() == null) {
			dao.adiciona(cheque);
			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", CADASTROU O CHEQUE: "
					+ cheque.getAluno().getNome() + ", " + cheque.getCurso().getNome());
			limpaFormulario();

			FacesUtil.addInfoMessage("Pagamento gravado com sucesso!");
			return "";
		} else {
			dao.altera(cheque);

			logger.info("USUÁRIO: " + usuarioLogado.getUsuario().getLogin() + ", ATUALIZOU O CHEQUE: "
					+ cheque.getAluno().getNome() + ", " + cheque.getCurso().getNome());
			FacesUtil.addInfoMessage("Pagamento atualizado com sucesso!");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

			return "pesquisaDadosBancarios?faces-redirect=true";
		}
	}

	private void limpaFormulario() {
		Cheque novoCheque = new Cheque();
		try {
			BeanUtils.copyProperties(novoCheque, this.cheque);
			novoCheque.setId(null);
			novoCheque.setNumCheque(null);
			novoCheque.setObs(null);
			novoCheque.getDataDeposito().add(Calendar.MONTH, 1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		this.cheque = novoCheque;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	public NomeDoBanco[] getNomeDosBancos() {
		return NomeDoBanco.values();
	}

}
