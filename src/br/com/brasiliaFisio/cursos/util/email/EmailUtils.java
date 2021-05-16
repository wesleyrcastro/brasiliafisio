package br.com.brasiliaFisio.cursos.util.email;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.modelo.Mensagem;
import br.com.brasiliaFisio.cursos.util.jsf.FacesUtil;
import br.com.brasiliaFisio.cursos.util.service.NegocioException;

public class EmailUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(EmailUtils.class);

	private static final String HOSTNAME = "smtp.brasiliafisio.fst.br";
	private static final String PORT = "587";
	private static final String USERNAME = "marketing@brasiliafisio.fst.br";
	private static final String PASSWORD = "Mo021292";
	private static final String EMAILORIGEM = "marketing@brasiliafisio.fst.br";

	public static void conectaEmail(Mensagem mensagem) {

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
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mensagem.getDestino()));
			message.setSubject(mensagem.getTitulo());
			message.setText(mensagem.getMensagem());

			Transport.send(message);
			
			FacesUtil.addInfoMessage("E-mail enviado com sucesso! " + mensagem.getDestino());
		} catch (MessagingException e) {
			log.error("Erro de e-mail: " + e.getMessage());
			throw new NegocioException("Falha ao enviar a mensagem.");
		}

	}

}