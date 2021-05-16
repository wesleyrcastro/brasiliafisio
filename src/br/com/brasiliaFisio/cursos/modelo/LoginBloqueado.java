package br.com.brasiliaFisio.cursos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@AuditTable(value = "LOGIN_BLOQUEADO_AUD")
@Table(name = "LOGIN_BLOQUEADO")
public class LoginBloqueado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@NotEmpty
	@Column(name = "IP", length = 70, nullable = false)
	private String ip;

	@NotNull(message = "Não pode estar vazio!")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA", nullable = false)
	private Date data;

	@NotEmpty
	@Column(name = "USUARIO", length = 50, nullable = false)
	private String usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
