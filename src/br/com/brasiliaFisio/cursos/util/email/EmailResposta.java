package br.com.brasiliaFisio.cursos.util.email;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.modelo.Inscricao;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;

public class EmailResposta implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(EmailResposta.class);

	private static final String HOSTNAME = "smtp.brasiliafisio.fst.br";
	private static final String PORT = "587";
	private static final String USERNAME = "marketing@brasiliafisio.fst.br";
	private static final String PASSWORD = "Mo021292";
	private static final String EMAILORIGEM = "marketing@brasiliafisio.fst.br";
//	private static final String DESTINO = "brasiliafisio@gmail.com";
	private static final String TITULO = "Cadastro da Pré-inscrição. [Mensagem enviada pelo administrador da BrasiliaFisio]";
	private static final String MENSAGEM = "Olá! Resposta da BrasiliaFisio em relação à sua observação.\n";

	public static void enviaEmail(Inscricao inscricao) {
		
		String destino = inscricao.getAluno().getEmail();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", HOSTNAME);
		props.put("mail.smtp.port", PORT);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(EMAILORIGEM));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
		message.setSubject(TITULO);
		message.setText(MENSAGEM + "Status da Pré-inscrição: " + inscricao.getObsBrasiliafisio());

		Transport.send(message);
		FacesUtil.addInfoMessage("Pré-inscrição atualizado com sucesso!");
		FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);

		}catch(Exception e) {
			log.error("Erro de e-mail: " + e.getMessage());
			FacesUtil.addWarnMessage("Ocorreu uma falha no envio da mensagem ao aluno, mas à observação da pré-inscrição foi alterada com sucesso! Confirme com aluno.");
			FacesUtil.facesContext().getExternalContext().getFlash().setKeepMessages(true);
		}
	}
}
