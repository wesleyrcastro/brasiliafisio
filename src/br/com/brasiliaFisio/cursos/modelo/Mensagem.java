package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Mensagem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message="E-mail de destino obrigatório")
	@Email
	@Length(max = 60, message = "O e-mail deve ter no máximo {max} caracteres")
	private String destino;

	@NotBlank(message="Título obrigatório")
	@Length(min = 3, max = 60, message = "O titulo deve ter entre {min} e {max} caracteres")
	private String titulo;

	@NotBlank(message="Mensagem obrigatório")
	@Length(min = 3, max = 300, message = "A mensagem deve ter entre {min} e {max} caracteres")
	private String mensagem;

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
